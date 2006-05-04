unit ProblemDM;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  comctrls, ADODB, Db, Criteria;

type
  TCriteriaType = (ctRoot, ctSimple, ctComplex);

  TdmProblem = class(TDataModule)
    opndlg: TOpenDialog;
    svdlg: TSaveDialog;
    cnctnSaveLoad: TADOConnection;
    cmd: TADOCommand;
    tbl: TADOTable;
    tblCR: TADOTable;
    tblLV: TADOTable;
    tblFP: TADOTable;
    procedure DataModuleDestroy(Sender: TObject);
  private
    FName: string;
    FAuthor: string;
    FComment: TStringList;
    FChanged: boolean;
    FOnChange: TNotifyEvent;
    FIniFileName: string;
    FCriteriaTree: TTreeNodes;
    FOnAddCriteria: TNotifyEvent;
    procedure SetChanged(Value: boolean);
    procedure SetProblemName(AName: string);
    procedure SetAuthor(AAuthor: string);
    procedure OnCommentChange(Sender: TObject);
    procedure OnCriteriaChange(Sender: TObject);
    procedure SaveTo(AIniFileName: string);
  public
    constructor Create(AOwner: TComponent; ACriteriaTree: TTreeNodes; AName:
      string); reintroduce; overload;
    constructor Create(AOwner: TComponent; ACriteriaTree: TTreeNodes);
      reintroduce; overload;

    procedure Save;
    procedure SaveAs;
    function Close: TModalResult;

    function AddCriteria(AParentNode: TTreeNode; ACriteriaType: TCriteriaType):
      TCriteria;
    procedure DeleteCriteria(ACriteriaNode: TTreeNode);

    property OnAddCriteria: TNotifyEvent read FOnAddCriteria write FOnAddCriteria;
    property Changed: boolean read FChanged write SetChanged;
    property OnChange: TNotifyEvent read FOnChange write FOnChange;
    property Name: string read FName write SetProblemName;
    property Author: string read FAuthor write SetAuthor;
    property Comment: TStringList read FComment;
  end;

implementation
uses inifiles, MainF, SCriteria, CCriteria, ConvFunc, Operator;

{$R *.DFM}
////////////////////////////////////////////////////////////////////////////////
function TdmProblem.AddCriteria(AParentNode: TTreeNode;
  ACriteriaType: TCriteriaType): TCriteria;
var
  NewNode: TTreeNode;
  ParentCriteria: TCCriteria;
  NewCriteria: TCriteria;
begin
  NewCriteria:= nil;
  // Отрабатываем действия необходимые для сторонних объектов
  if Assigned(FOnAddCriteria) then FOnAddCriteria(Self);
  // Получаем критерий для которого необходимо создать дочерний
  if Assigned(AParentNode) then
    ParentCriteria:= AParentNode.Data
  else ParentCriteria:= nil;
  // Если этот критерий комплексный то можно добавлять
  if (ParentCriteria is TCCriteria) or (ACriteriaType = ctRoot) then
  begin
    // Создаем новый узел
    NewNode:= FCriteriaTree.AddChild(AParentNode,'');

    case ACriteriaType of
      // Создаем интегральный критерий привязанный к новому узлу
      ctRoot: NewCriteria:= TCCriteria.Create(NewNode, 'интегральный критерий');
      // Создаем единичный критерий привязанный к новому узлу
      ctSimple: NewCriteria:= TSCriteria.Create(NewNode, 'единичный критерий');
      // Создаем комплексный критерий привязанный к новому узлу
      ctComplex: NewCriteria:= TCCriteria.Create(NewNode, 'комплексный критерий');
    end; // case ACriteriaType
    NewCriteria.OnChange:= Self.OnCriteriaChange; 
    if Assigned(ParentCriteria) then
    begin
      // Привязываем обработчики событий к новому критерию
      NewCriteria.OnWeightChange:= ParentCriteria.OnChildWeightChange;
      // Стандартизуем веса всех дочерних критериев
      ParentCriteria.OnChildWeightChange(NewCriteria);
    end;
    // Устанавливаем фокус в TreeView на новый узел
    FCriteriaTree.Owner.Selected:= NewNode;
  end;
  Result:= NewCriteria;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TdmProblem.SetChanged( Value: boolean );
begin
  FChanged:= Value;
  if Assigned( FOnChange ) then FOnChange( Self );
end;

////////////////////////////////////////////////////////////////////////////////
procedure TdmProblem.SetProblemName( AName: string );
begin
  FName:= AName;
  Changed:= TRUE;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TdmProblem.SetAuthor( AAuthor: string );
begin
  FAuthor:= AAuthor;
  Changed:= TRUE;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TdmProblem.OnCommentChange(Sender: TObject);
begin
  Changed:= TRUE;
end;

////////////////////////////////////////////////////////////////////////////////
constructor TdmProblem.Create(AOwner: TComponent; ACriteriaTree: TTreeNodes;
  AName: string);
begin
  inherited Create( AOwner );
  FCriteriaTree:= ACriteriaTree;
  Changed:= TRUE;
  FName:= AName;
  FIniFileName:= '';
  FAuthor:= '';
  FComment:= TStringList.Create;
  FComment.OnChange:= Self.OnCommentChange;
  // Добавляем вершину дерева критериев
  AddCriteria(nil, ctRoot);
end;

////////////////////////////////////////////////////////////////////////////////
constructor TdmProblem.Create(AOwner: TComponent; ACriteriaTree: TTreeNodes);
const
  dBaseStr: string = 'Provider=MSDASQL.1;Persist Security Info=False;Data Source=dBASE Files;Initial Catalog=%0:s';
var
  IniFile: TIniFile;
  Path: string;
  BaseFileName: string;
  DataConnectionString: string;
  NameField: string;
  DataTableName: string;
  i: Integer;
  CriteriaType: Integer;
  CriteriaIndex: Integer;
  ParentIndex: Integer;
  NewCriteria: TCriteria;
begin
  inherited Create( AOwner );

  IniFile:= nil;
  FCriteriaTree:= ACriteriaTree;
  opndlg.InitialDir:= GetCurrentDir;
  if opndlg.Execute then
    begin
        FIniFileName:= opndlg.FileName;
        Path:= ExtractFileDir(opndlg.FileName);
        BaseFileName:= ChangeFileExt(ExtractFileName(opndlg.FileName),'');

        try
          IniFile:= TIniFile.Create( FIniFileName );
          FName:= IniFile.ReadString('COMMON', 'Problem Name', 'Новая задача');
          FAuthor:= IniFile.ReadString('COMMON', 'Author', '');

          DataConnectionString:= IniFile.ReadString('DATA', 'Connection string', '');
          NameField:= IniFile.ReadString('DATA', 'Name Field', '');
          DataTableName:= IniFile.ReadString('DATA', 'Data Table Name', '');

          FComment:= TStringList.Create;
          FComment.LoadFromFile(IniFile.ReadString('COMMON', 'Description File', ''));
          FComment.OnChange:= Self.OnCommentChange;
          FChanged:= FALSE;
        finally
          IniFile.Free;
        end;
      // Откроем данные для оценки если на момент сохранения они были открыты
      if DataConnectionString <> '' then
      with FMain.frmObject do
      begin
        // Закроем набор данных если он открыт
        if cnctnData.Connected then cnctnData.Connected:= FALSE;
        cnctnData.ConnectionString:= DataConnectionString;
        // Откроем новый набор данных
        cnctnData.Connected:= TRUE;
        tblData.TableName:= DataTableName;
        try
          tblData.Active:= TRUE;
          for i:= 0 to cbxNameField.Items.Count - 1 do
            if cbxNameField.Items[i] = NameField then
            begin
               cbxNameField.ItemIndex:= i;
               Break;
            end;
        except
          cnctnData.Connected:= FALSE; // Закрываем соединение
          MessageDlg('Структура данных нарушена или'#13
            + 'не соответсвует выбранному типу', mtInformation, [mbOK], 0);
        end;
      end;

      //DONE: выполнить загрузку данных из таблиц
      // 1. Установим ADO соединение
      if cnctnSaveLoad.Connected then cnctnSaveLoad.Close;
      cnctnSaveLoad.ConnectionString:= Format(dBaseStr, [Path]);
      cnctnSaveLoad.Connected:= TRUE;
      // 2. Откроем таблицы данных
      tblCR.TableName:= BaseFileName + '_CR';
      tblCR.Open;
      tblLV.TableName:= BaseFileName + '_LV';
      tblLV.Open;
      tblFP.TableName:= BaseFileName + '_FP';
      tblFP.Open;
      // 3. Заполним дерево критериев
      while not tblCR.Eof do
      begin
        CriteriaType:= tblCR.FieldByName('m_type').AsInteger;
        CriteriaIndex:= tblCR.FieldByName('m_id').AsInteger;
        ParentIndex:= tblCR.FieldByName('m_parent').AsInteger;
        case CriteriaType of
          100: // Комплексный критерий
            begin
              if ParentIndex <> -1 then
                NewCriteria:= AddCriteria(FCriteriaTree.Item[ParentIndex], ctComplex)
              else
                NewCriteria:= AddCriteria(nil, ctRoot);
              NewCriteria.Name:= tblCR.FieldByName('m_name').AsString;
              NewCriteria.Weight:= tblCR.FieldByName('m_weight').AsFloat;
              NewCriteria.Comment.Text:= tblCR.FieldByName('m_comment').AsString;
              case tblCR.FieldByName('m_ftype').AsInteger of
                0: // линейный оператор агрегирования
                  begin
                    TCCriteria(NewCriteria).ChangeToAdditive;
                  end;
                1: // степенной I-типа
                  begin
                    TCCriteria(NewCriteria).ChangeToPowerI;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'L'])
                      ,[loCaseInsensitive]);}
                    TPowerOpr(TCCriteria(NewCriteria).Operator).Lambda:=
                      tblFP.FieldByName('m_value').AsFloat;
                  end;
                2: // степенной II-типа
                  begin
                    TCCriteria(NewCriteria).ChangeToPowerII;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'L'])
                      ,[loCaseInsensitive]); }
                    TPowerOpr(TCCriteria(NewCriteria).Operator).Lambda:=
                      tblFP.FieldByName('m_value').AsFloat;
                  end;
                3: // двойной степенной
                  begin
                    TCCriteria(NewCriteria).ChangeToDoublePower;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'L'])
                      ,[loCaseInsensitive]);}
                    TPowerOpr(TCCriteria(NewCriteria).Operator).Lambda:=
                      tblFP.FieldByName('m_value').AsFloat;
                  end;
                4: // мультипликативный
                  begin
                    TCCriteria(NewCriteria).ChangeToMultiplicative;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'L'])
                      ,[loCaseInsensitive]);}
                    TMultiplicativeOpr(TCCriteria(NewCriteria).Operator).Lambda:=
                      tblFP.FieldByName('m_value').AsFloat;
                  end;
              end;
            end;
          0,1: // Единичный критерий, возрастающее и убывающее поведение
            begin
              NewCriteria:= AddCriteria(FCriteriaTree.Item[ParentIndex], ctSimple);

              NewCriteria.Name:= tblCR.FieldByName('m_name').AsString;
              TSCriteria(NewCriteria).DataField:=
                tblCR.FieldByName('m_data').AsString;
              NewCriteria.Weight:= tblCR.FieldByName('m_weight').AsFloat;
              NewCriteria.Comment.Text:= tblCR.FieldByName('m_comment').AsString;
              TSCriteria(NewCriteria).Behavior:= CriteriaType;
{TODO              tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'min'])
                ,[loCaseInsensitive]);}
              TSCriteria(NewCriteria).MinValue:= tblFP.FieldByName('m_value').AsFloat;
{TODO              tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'max'])
                ,[loCaseInsensitive]);}
              TSCriteria(NewCriteria).MaxValue:= tblFP.FieldByName('m_value').AsFloat;
              case tblCR.FieldByName('m_ftype').AsInteger of
                0: // линейная функция перевода
                  begin
                    TSCriteria(NewCriteria).ChangeToLiner;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'LS'])
                      ,[loCaseInsensitive]);}
                    TLinerFN(TSCriteria(NewCriteria).ConvFunc).LSatiation:=
                      tblFP.FieldByName('m_value').AsFloat;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'RS'])
                      ,[loCaseInsensitive]);}
                    TLinerFN(TSCriteria(NewCriteria).ConvFunc).RSatiation:=
                      tblFP.FieldByName('m_value').AsFloat;
                  end;
                1: // бетта распределения функция перевода
                  begin
                    TSCriteria(NewCriteria).ChangeToBetaDistribution;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'P'])
                      ,[loCaseInsensitive]);}
                    TBetaDistributionFN(TSCriteria(NewCriteria).ConvFunc).P:=
                      tblFP.FieldByName('m_value').AsFloat;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'Q'])
                      ,[loCaseInsensitive]);}
                    TBetaDistributionFN(TSCriteria(NewCriteria).ConvFunc).Q:=
                      tblFP.FieldByName('m_value').AsFloat;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'LS'])
                      ,[loCaseInsensitive]);}
                    TBetaDistributionFN(TSCriteria(NewCriteria).ConvFunc).LSatiation:=
                      tblFP.FieldByName('m_value').AsFloat;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'RS'])
                      ,[loCaseInsensitive]);}
                    TBetaDistributionFN(TSCriteria(NewCriteria).ConvFunc).RSatiation:=
                      tblFP.FieldByName('m_value').AsFloat;
                  end;
                2: // гаусова функция перевода
                  begin
                    TSCriteria(NewCriteria).ChangeToGaussian;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'LS'])
                      ,[loCaseInsensitive]);}
                    TGaussianFN(TSCriteria(NewCriteria).ConvFunc).LSatiation:=
                      tblFP.FieldByName('m_value').AsFloat;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'RS'])
                      ,[loCaseInsensitive]);}
                    TGaussianFN(TSCriteria(NewCriteria).ConvFunc).RSatiation:=
                      tblFP.FieldByName('m_value').AsFloat;
                  end;
                3: // бетта функция перевода
                  begin
                    TSCriteria(NewCriteria).ChangeToBeta;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'P'])
                      ,[loCaseInsensitive]);}
                    TBetaFN(TSCriteria(NewCriteria).ConvFunc).P:=
                      tblFP.FieldByName('m_value').AsFloat;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'Q'])
                      ,[loCaseInsensitive]);}
                    TBetaFN(TSCriteria(NewCriteria).ConvFunc).Q:=
                      tblFP.FieldByName('m_value').AsFloat;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'LS'])
                      ,[loCaseInsensitive]);}
                    TBetaFN(TSCriteria(NewCriteria).ConvFunc).LSatiation:=
                      tblFP.FieldByName('m_value').AsFloat;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'RS'])
                      ,[loCaseInsensitive]);}
                    TBetaFN(TSCriteria(NewCriteria).ConvFunc).RSatiation:=
                      tblFP.FieldByName('m_value').AsFloat;
                  end;
                4: // показательная функция перевода
                  begin
                    TSCriteria(NewCriteria).ChangeToExponential;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'A'])
                      ,[loCaseInsensitive]);}
                    TExponentialFN(TSCriteria(NewCriteria).ConvFunc).Base:=
                      tblFP.FieldByName('m_value').AsFloat;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'LS'])
                      ,[loCaseInsensitive]); }
                    TExponentialFN(TSCriteria(NewCriteria).ConvFunc).LSatiation:=
                      tblFP.FieldByName('m_value').AsFloat;
{TODO                    tblFP.Locate('m_node_id;m_name', VarArrayOf([CriteriaIndex,'RS'])
                      ,[loCaseInsensitive]); }
                    TExponentialFN(TSCriteria(NewCriteria).ConvFunc).RSatiation:=
                      tblFP.FieldByName('m_value').AsFloat;
                  end;
              end;
            end;
          2: // Единичный критерий, качественное поведение
            begin
              NewCriteria:= AddCriteria(FCriteriaTree.Item[ParentIndex], ctSimple);

              NewCriteria.Name:= tblCR.FieldByName('m_name').AsString;
              TSCriteria(NewCriteria).DataField:=
                tblCR.FieldByName('m_data').AsString;
              NewCriteria.Weight:= tblCR.FieldByName('m_weight').AsFloat;
              NewCriteria.Comment.Text:= tblCR.FieldByName('m_comment').AsString;
              TSCriteria(NewCriteria).Behavior:= 2;
              tblLV.First;
              tblLV.Locate('m_node_id', CriteriaIndex, []);
              while (tblLV.FieldByName('m_node_id').AsInteger = CriteriaIndex)
                and not tblLV.Eof do
              begin
                TSCriteria(NewCriteria).ValueList.Add(
                  tblLV.FieldByName('m_value').AsString,
                  tblLV.FieldByName('m_profit').AsFloat );
                tblLV.Next;
              end;
            end;
        end;
        tblCR.Next;
      end;
      // Сфокусируем на интегральном критериии
      FCriteriaTree.Owner.Selected:= FCriteriaTree.Item[0];
      // Закроем таблицы данных
      tblCR.Close;
      tblLV.Close;
      tblFP.Close;
      // Закроем ADO соединение
      cnctnSaveLoad.Close;
    end
  else Abort;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TdmProblem.DataModuleDestroy(Sender: TObject);
begin
  FCriteriaTree.Clear;
  FComment.Free;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TdmProblem.SaveTo(AIniFileName: string);
const
  dBaseStr: string = 'Provider=MSDASQL.1;Persist Security Info=False;Data Source=dBASE Files;Initial Catalog=%0:s';
  CreateNodeTblStr: string = 'CREATE TABLE "%0:s" ( m_id INTEGER, m_name CHAR(35), m_data CHAR(20), m_weight INTEGER, m_parent INTEGER, m_type INTEGER, m_ftype INTEGER, m_comment MEMO)';
  CreateLValueTblStr: string = 'CREATE TABLE "%0:s" ( m_node_id INTEGER, m_value CHAR(30), m_profit INTEGER)';
  CreateFuncTblStr: string = 'CREATE TABLE "%0:s" ( m_node_id INTEGER, m_name CHAR(10), m_value INTEGER)';
  DropTblStr: string = 'DROP TABLE %0:s';
var
  IniFile: TIniFile;
  Path: string;
  BaseFileName: string;
  DescriptionFileName: string;
  ConnectionString: string;
  DataTableName: string;
  NameField: string;
  TableName: string;
  i, j: Integer;
  Criteria: TCriteria;
procedure ExecSQL( ACommand: string);
begin
  cmd.CommandText:= ACommand;
  cmd.Execute;
end;
begin
    IniFile:= nil;
    try
      IniFile:= TIniFile.Create( AIniFileName );
      
      BaseFileName:= ChangeFileExt(ExtractFileName(AIniFileName),'');
      Path:= ExtractFileDir(AIniFileName);

      IniFile.WriteString('COMMON','Problem Name', FName);
      IniFile.WriteString('COMMON','Author', FAuthor);

      DescriptionFileName:= BaseFileName + '.txt';
      FComment.SaveToFile(DescriptionFileName);
      IniFile.WriteString('COMMON','Description File', DescriptionFileName);
      // Сохраним информацию о подключении данных для оценки
      if fMain.frmObject.tblData.Active then
      begin
        ConnectionString:= fMain.frmObject.cnctnData.ConnectionString;
        DataTableName:= fMain.frmObject.tblData.TableName;
        NameField:= fMain.frmObject.cbxNameField.Text;
      end
      else
      begin
        ConnectionString:= '';
        DataTableName:= '';
        NameField:= '';
      end;
      IniFile.WriteString('DATA','Connection string', ConnectionString);
      IniFile.WriteString('DATA','Data Table Name', DataTableName);
      IniFile.WriteString('DATA','Name Field', NameField);
      // Сохраним дерево критериев
      // 1. Установим ADO соединение
      if cnctnSaveLoad.Connected then cnctnSaveLoad.Close;
      cnctnSaveLoad.ConnectionString:= Format(dBaseStr, [Path]);
      cnctnSaveLoad.Connected:= TRUE;
      // 2. Создадим таблицу для хранения критериев
      TableName:= BaseFileName + '_CR';
      try
        ExecSQL( Format(CreateNodeTblStr, [TableName]) );
      except
        ExecSQL( Format(DropTblStr, [TableName]) );
        ExecSQL( Format(CreateNodeTblStr, [TableName]) );
      end;
      tbl.TableName:= TableName;
      try
        tbl.Open;
        for i:= 0 to FCriteriaTree.Count - 1 do
        begin   // ttreenodes
        ////////////////////////////////////////////////////////////////////////
          tbl.Insert;
          tbl.FieldByName('m_id').AsInteger:= FCriteriaTree.Item[i].AbsoluteIndex;
          Criteria:= FCriteriaTree.Item[i].Data;
          tbl.FieldByName('m_name').AsString:= Criteria.Name;
          tbl.FieldByName('m_weight').AsFloat:= Criteria.Weight;
          tbl.FieldByName('m_comment').AsString:= Criteria.Comment.Text;
          if Assigned(FCriteriaTree.Item[i].Parent) then
            tbl.FieldByName('m_parent').AsInteger:=
             FCriteriaTree.Item[i].Parent.AbsoluteIndex
          else tbl.FieldByName('m_parent').AsInteger:= -1;

          if Criteria is TSCriteria then
          begin
            tbl.FieldByName('m_data').AsString:= TSCriteria(Criteria).DataField;
            tbl.FieldByName('m_type').AsInteger:= TSCriteria(Criteria).Behavior;
            case TSCriteria(Criteria).Behavior of
              0,1: tbl.FieldByName('m_ftype').AsInteger:=
                TSCriteria(Criteria).ConvFunc.ID;
              2: tbl.FieldByName('m_ftype').AsInteger:= -1;
            end
          end
          else
          begin
            tbl.FieldByName('m_type').AsInteger:= 100;
            tbl.FieldByName('m_ftype').AsInteger:=
              TCCriteria(Criteria).Operator.ID;
          end;
          tbl.Post;
        ////////////////////////////////////////////////////////////////////////
        end;
      finally
        tbl.Close;
      end;
      // 3. Создадим таблицу для хранения допустимых значений критериев
      TableName:= BaseFileName + '_LV';
      try
        ExecSQL( Format(CreateLValueTblStr, [TableName]) );
      except
        ExecSQL( Format(DropTblStr, [TableName]) );
        ExecSQL( Format(CreateLValueTblStr, [TableName]) );
      end;
      //DONE: сохранение допустимых значений единичных критериев
      tbl.TableName:= TableName;
      try
        tbl.Open;
        for i:= 0 to FCriteriaTree.Count - 1 do
        begin
        ////////////////////////////////////////////////////////////////////////
          Criteria:= FCriteriaTree.Item[i].Data;
          if Criteria is TSCriteria then
            if TSCriteria(Criteria).Behavior = 2 then // если качественный
              for j:= 0 to TSCriteria(Criteria).ValueList.Count-1 do
              begin
                tbl.Insert;
                tbl.FieldByName('m_node_id').AsInteger:=
                  FCriteriaTree.Item[i].AbsoluteIndex;
                tbl.FieldByName('m_value').AsString:=
                  TSValue(TSCriteria(Criteria).ValueList[j]).Value;
                tbl.FieldByName('m_profit').AsFloat:=
                  TSValue(TSCriteria(Criteria).ValueList[j]).Profit;
                tbl.Post;
              end;
        ////////////////////////////////////////////////////////////////////////
        end;
      finally
        tbl.Close;
      end;

      // 4. Создадим таблицу для хранения параметров функций
      TableName:= BaseFileName + '_FP';
      try
        ExecSQL( Format(CreateFuncTblStr, [TableName]) );
      except
        ExecSQL( Format(DropTblStr, [TableName]) );
        ExecSQL( Format(CreateFuncTblStr, [TableName]) );
      end;
      tbl.TableName:= TableName;
      //DONE: сохранение параметров функций
      try
        tbl.Open;
        for i:= 0 to FCriteriaTree.Count - 1 do
        begin
        ////////////////////////////////////////////////////////////////////////
          Criteria:= FCriteriaTree.Item[i].Data;
          if Criteria is TSCriteria then
          begin
            if TSCriteria(Criteria).Behavior in [0,1] then
            begin
              tbl.Insert;
              tbl.FieldByName('m_node_id').AsInteger:=
                FCriteriaTree.Item[i].AbsoluteIndex;
              tbl.FieldByName('m_name').AsString:= 'min';
              tbl.FieldByName('m_value').AsFloat:=
                 TFunc(TSCriteria(Criteria).ConvFunc).KMin;
              tbl.Post;

              tbl.Insert;
              tbl.FieldByName('m_node_id').AsInteger:=
                FCriteriaTree.Item[i].AbsoluteIndex;
              tbl.FieldByName('m_name').AsString:= 'max';
                tbl.FieldByName('m_value').AsFloat:=
              TFunc(TSCriteria(Criteria).ConvFunc).KMax;
              tbl.Post;

              case TSCriteria(Criteria).ConvFunc.ID of
                0:begin
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'LS';
                    tbl.FieldByName('m_value').AsFloat:=
                      TLinerFN(TSCriteria(Criteria).ConvFunc).LSatiation;
                     tbl.Post;
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'RS';
                    tbl.FieldByName('m_value').AsFloat:=
                      TLinerFN(TSCriteria(Criteria).ConvFunc).RSatiation;
                     tbl.Post;
                  end;
                1:begin
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'P';
                    tbl.FieldByName('m_value').AsFloat:=
                      TBetaDistributionFN(TSCriteria(Criteria).ConvFunc).P;
                    tbl.Post;
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'Q';
                    tbl.FieldByName('m_value').AsFloat:=
                      TBetaDistributionFN(TSCriteria(Criteria).ConvFunc).Q;
                     tbl.Post;
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'LS';
                    tbl.FieldByName('m_value').AsFloat:=
                      TBetaDistributionFN(TSCriteria(Criteria).ConvFunc).LSatiation;
                     tbl.Post;
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'RS';
                    tbl.FieldByName('m_value').AsFloat:=
                      TBetaDistributionFN(TSCriteria(Criteria).ConvFunc).RSatiation;
                     tbl.Post;
                    end;
                2:begin
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'LS';
                    tbl.FieldByName('m_value').AsFloat:=
                      TGaussianFN(TSCriteria(Criteria).ConvFunc).LSatiation;
                     tbl.Post;
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'RS';
                    tbl.FieldByName('m_value').AsFloat:=
                      TGaussianFN(TSCriteria(Criteria).ConvFunc).RSatiation;
                     tbl.Post;
                  end;
                 3:
                  begin
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'P';
                    tbl.FieldByName('m_value').AsFloat:=
                      TBetaFN(TSCriteria(Criteria).ConvFunc).P;
                    tbl.Post;
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'Q';
                    tbl.FieldByName('m_value').AsFloat:=
                      TBetaFN(TSCriteria(Criteria).ConvFunc).Q;
                     tbl.Post;
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'LS';
                    tbl.FieldByName('m_value').AsFloat:=
                      TBetaFN(TSCriteria(Criteria).ConvFunc).LSatiation;
                     tbl.Post;
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'RS';
                    tbl.FieldByName('m_value').AsFloat:=
                      TBetaFN(TSCriteria(Criteria).ConvFunc).RSatiation;
                     tbl.Post;
                  end;
                4:
                  begin
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'A';
                    tbl.FieldByName('m_value').AsFloat:=
                      TExponentialFN(TSCriteria(Criteria).ConvFunc).Base;
                    tbl.Post;
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'LS';
                    tbl.FieldByName('m_value').AsFloat:=
                      TExponentialFN(TSCriteria(Criteria).ConvFunc).LSatiation;
                     tbl.Post;
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'RS';
                    tbl.FieldByName('m_value').AsFloat:=
                      TExponentialFN(TSCriteria(Criteria).ConvFunc).RSatiation;
                     tbl.Post;
                  end;

              end;

            end;
          end
          else // Комплексные критерий
          begin
            case TCCriteria(Criteria).Operator.ID of
              1,2,3:
                 begin
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'L';
                    tbl.FieldByName('m_value').AsFloat:=
                      TPowerOpr(TCCriteria(Criteria).Operator).Lambda;
                    tbl.Post;
                 end;
              4:
                 begin
                    tbl.Insert;
                    tbl.FieldByName('m_node_id').AsInteger:=
                      FCriteriaTree.Item[i].AbsoluteIndex;
                    tbl.FieldByName('m_name').AsString:= 'L';
                    tbl.FieldByName('m_value').AsFloat:=
                      TMultiplicativeOpr(TCCriteria(Criteria).Operator).Lambda;
                    tbl.Post;
                 end;
            end;
          end;
        ////////////////////////////////////////////////////////////////////////
        end;
      finally
        tbl.Close;
      end;

      IniFile.WriteString('DATA','Criteria File', BaseFileName + '_CR.dbf');
      IniFile.WriteString('DATA','Legitimate Value File', BaseFileName + '_LV.dbf');
      IniFile.WriteString('DATA','Fuction Parametr File', BaseFileName + '_FP.dbf');

      FIniFileName:= AIniFileName;
      // Если дошли сюда, значит сохранили нормально
      cnctnSaveLoad.Close;
      Changed:= FALSE;
    finally
      IniFile.Free;
    end;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TdmProblem.SaveAs;
begin
  svdlg.InitialDir:= GetCurrentDir;
  if svdlg.Execute then SaveTo( svdlg.FileName );
end;

////////////////////////////////////////////////////////////////////////////////
procedure TdmProblem.Save;
begin
  // Определяем была задача создана или открыта существующая
  // В зависимости от этого используем разные методы сохранения
  // Критерием при определении служит поле FIniFileName
  // Если поле равно пустой строке, то задача была создана и ни разу не
  // сохранялась
  if FIniFileName <> '' then
    SaveTo(FIniFileName)
  else SaveAs;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TdmProblem.DeleteCriteria(ACriteriaNode: TTreeNode);
var
  ParentCriteria: TCCriteria;
begin
  // Нельзя улалять корень дерева критериев
  if ACriteriaNode.Parent <> nil then
  begin
    // Получим критерий соответствующий узлу-родителю удаляемого узла
    ParentCriteria:= TCCriteria(ACriteriaNode.Parent.Data);
    // Установим выбранным родительский узел
    FCriteriaTree.Owner.Selected:= ACriteriaNode.Parent;
    // Удалим узел
    FCriteriaTree.Delete(ACriteriaNode);
    // Стандартизуем веса оставшхся дочерних критериев
    ParentCriteria.OnChildWeightChange(ParentCriteria);
  end;
end;

////////////////////////////////////////////////////////////////////////////////
function TdmProblem.Close: TModalResult;
begin
  Result:= mrOK;
  if FChanged then
  case MessageDlg('Сохранить задачу?', mtConfirmation, [mbNo, mbYes, mbCancel], 0) of
    mrYes:
      try
        Save;
        Free;
      except
         Result:= mrAbort;
      end;
    mrNo: Free;
    mrCancel: Result:= mrCancel;
  end
  else Free;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TdmProblem.OnCriteriaChange(Sender: TObject);
begin
  Changed:= TRUE;
end;

end.
