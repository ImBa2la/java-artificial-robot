unit CriteriaFrm;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  StdCtrls, ComCtrls, ExtCtrls, ImgList, Criteria,
  SCriteria, SCCnvrsnPrmtrFrm, SCValuesFrm, TeEngine, Series, TeeProcs,
  Chart, LinerFuncFrm, math, BetaDistributionFuncFrm, ConvFunc, BetaFuncFrm,
  ExponentialFuncFrm, GaussianFuncFrm;

type
  TfrmCriteria = class(TFrame)
    pnl: TPanel;
    trvw: TTreeView;
    imglst: TImageList;
    pnlPropertes: TPanel;
    pgcntrl: TPageControl;
    tbshtCCProperties: TTabSheet;
    lblCCName: TLabel;
    lblCCWeight: TLabel;
    lblCCOperator: TLabel;
    edtCCriteriaName: TEdit;
    edtCWeight: TEdit;
    cmbxAOperator: TComboBox;
    tbshtSCProperties: TTabSheet;
    lblSCName: TLabel;
    lblSCWeight: TLabel;
    lblSCFBehavior: TLabel;
    edtSCriteriaName: TEdit;
    edtSWeight: TEdit;
    cmbxFBehavior: TComboBox;
    tbshtComment: TTabSheet;
    rchdtComment: TRichEdit;
    pnlOKCancelBtn: TPanel;
    btnApply: TButton;
    btnCancel: TButton;
    tbshtConvFunction: TTabSheet;
    chrt: TChart;
    Series: TLineSeries;
    pnlForFuncFrame: TPanel;
    frmLinerFunc: TfrmLinerFunc;
    frmBetaDistributionFunc: TfrmBetaDistributionFunc;
    cmbxDataField: TComboBox;
    lblDataField: TLabel;
    edtLambda: TEdit;
    lblLambda: TLabel;
    pnl1: TPanel;
    frmSCCnvrsnPrmtr: TfrmSCCnvrsnPrmtr;
    frmSCValues: TfrmSCValues;
    tbshtAOperator: TTabSheet;
    chrtAOperator: TChart;
    SeriesF: TLineSeries;
    SeriesG: TLineSeries;
    frmBetaFunc: TfrmBetaFunc;
    frmExponentialFunc: TfrmExponentialFunc;
    frmGaussianFunc: TfrmGaussianFunc;
    procedure OnNodeSelection(Sender: TObject; Node: TTreeNode);
    procedure OnNodeDeletion(Sender: TObject; Node: TTreeNode);
    procedure OnBehaviorChange(Sender: TObject);
    procedure OnNodeCollapsing(Sender: TObject; Node: TTreeNode;
      var AllowCollapse: Boolean);
    procedure OnSCConvFunctionChange(Sender: TObject);
    procedure OnSCMaxChange(Sender: TObject);
    procedure OnSCMinChange(Sender: TObject);
    procedure OnFuncTabSheetShow(Sender: TObject);
    procedure RefreshView(Sender: TObject);
    procedure OnSCNameChange(Sender: TObject);
    procedure OnSCWeightChange(Sender: TObject);
    procedure OnCCNameChange(Sender: TObject);
    procedure OnCCWeightChange(Sender: TObject);
    procedure OnCommentChange(Sender: TObject);
    procedure OnCommentExit(Sender: TObject);
    procedure SetToChanged(Sender: TObject);
    procedure ApplyChanges(Sender: TObject);    
    procedure CancelChanges(Sender: TObject);
    procedure OnSCPChange(Sender: TObject);
    procedure OnSCQChange(Sender: TObject);
    procedure OnCCOperatorChange(Sender: TObject);
    procedure OnStrgrdValueKeyDown(Sender: TObject; var Key: Word;
      Shift: TShiftState);
    procedure frmSCValuesstrgrdValueSelectCell(Sender: TObject; ACol,
      ARow: Integer; var CanSelect: Boolean);
    procedure frmSCValuesstrgrdValueDblClick(Sender: TObject);
    procedure cmbxDataFieldChange(Sender: TObject);
    procedure frmSCValuesbtnFillClick(Sender: TObject);
    procedure OnCCLambdaChange(Sender: TObject);
    procedure tbshtAOperatorShow(Sender: TObject);
    procedure frmBetaFuncedtPChange(Sender: TObject);
    procedure frmBetaFuncedtQChange(Sender: TObject);
    procedure frmExponentialFuncedtBaseChange(Sender: TObject);
    procedure frmBetaDistributionFuncedtLSatiationChange(Sender: TObject);
    procedure frmBetaDistributionFuncedtRSatiationChange(Sender: TObject);
    procedure frmBetaFuncedtLSatiationChange(Sender: TObject);
    procedure frmBetaFuncedtRSatiationChange(Sender: TObject);
    procedure frmLinerFuncedtLSatiationChange(Sender: TObject);
    procedure frmLinerFuncedtRSatiationChange(Sender: TObject);
    procedure frmGaussianFuncedtLSatiationChange(Sender: TObject);
    procedure frmGaussianFuncedtRSatiationChange(Sender: TObject);
    procedure frmExponentialFuncedtLSatiationChange(Sender: TObject);
    procedure frmExponentialFuncedtRSatiationChange(Sender: TObject);
  private
    // поле указывающее на необходимость обработки изменения в элементах управления
    FChangeProcessing: boolean;
    FDefaultPage: boolean;
    FCurrentCriteria: TCriteria;
    FOnSelect: TTVChangedEvent;
    FOnCriteriaChange: TNotifyEvent;
    FOnApplyChanges: TNotifyEvent;
    FOnCancelChanges: TNotifyEvent;
    procedure EnableChangeProcessing;
    procedure DisableChangeProcessing;
    function GetSCMinValue: double;
    procedure SetSCMinValue(AValue: double);
    function GetSCMaxValue: double;
    procedure SetSCMaxValue(AValue: double);
    procedure RefreshFuncTabSheet;
    procedure RefreshValueList;
    procedure RefreshValueConstraint;
    procedure RepaintChart;
    procedure ViewSimple;
    procedure ViewComplex;
    procedure AddSValue;
  public
    constructor Create(AOwner: TComponent); override;
    procedure OnAddCriteria(Sender: TObject);
    property OnSelect:TTVChangedEvent read FOnSelect write FOnSelect;
    property OnCriteriaChange:TNotifyEvent read FOnCriteriaChange write
      FOnCriteriaChange;
    property OnApplyChanges:TNotifyEvent read FOnApplyChanges write
      FOnApplyChanges;
    property OnCancelChanges:TNotifyEvent read FOnCancelChanges write
      FOnCancelChanges;
    property Selected: TCriteria read FCurrentCriteria;
    property SCMinValue: double read GetSCMinValue write SetSCMinValue;
    property SCMaxValue: double read GetSCMaxValue write SetSCMaxValue;
    procedure UpdateSValue;
    procedure DeleteSValue;
  end;

implementation

uses CCriteria, SCValueDlg, MainF, DB, ObjectFrm, Operator;

{$R *.DFM}
////////////////////////////////////////////////////////////////////////////////
constructor TfrmCriteria.Create(AOwner: TComponent);
begin
  inherited Create( AOwner );
  EnableChangeProcessing;
  FDefaultPage:= TRUE;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnAddCriteria(Sender: TObject);
begin
  FDefaultPage:= TRUE;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.EnableChangeProcessing;
begin
  FChangeProcessing:= TRUE;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.DisableChangeProcessing;
begin
  FChangeProcessing:= FALSE;
end;
////////////////////////////////////////////////////////////////////////////////
function TfrmCriteria.GetSCMinValue: double;
begin
  Result:= StrToFloat(frmSCCnvrsnPrmtr.edtSMin.Text);
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.SetSCMinValue(AValue: double);
var
  OldEvent: TNotifyEvent;
begin
  if FChangeProcessing then
    frmSCCnvrsnPrmtr.edtSMin.Text:= FloatToStr(AValue)
  else
  begin
    OldEvent:= frmSCCnvrsnPrmtr.edtSMin.OnChange;
    frmSCCnvrsnPrmtr.edtSMin.OnChange:= nil;
    frmSCCnvrsnPrmtr.edtSMin.Text:= FloatToStr(AValue);
    frmSCCnvrsnPrmtr.edtSMin.OnChange:= OldEvent;
  end;
end;

function TfrmCriteria.GetSCMaxValue: double;
begin
  Result:= StrToFloat(frmSCCnvrsnPrmtr.edtSMax.Text);
end;

procedure TfrmCriteria.SetSCMaxValue(AValue: double);
var
  OldEvent: TNotifyEvent;
begin
  if FChangeProcessing then
    frmSCCnvrsnPrmtr.edtSMax.Text:= FloatToStr(AValue)
  else
  begin
    OldEvent:= frmSCCnvrsnPrmtr.edtSMax.OnChange;
    frmSCCnvrsnPrmtr.edtSMax.OnChange:= nil;
    frmSCCnvrsnPrmtr.edtSMax.Text:= FloatToStr(AValue);
    frmSCCnvrsnPrmtr.edtSMax.OnChange:= OldEvent;
  end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.RefreshValueList;
var
  i: Integer;
  v: TSValue;
begin
  frmSCValues.strgrdValue.RowCount:=
    Max(TSCriteria(Selected).ValueList.Count + 1, 2);
    frmSCValues.strgrdValue.Cells[0,1]:= '';
    frmSCValues.strgrdValue.Cells[1,1]:= '';
    frmSCValues.strgrdValue.Cells[2,1]:= '';    
  for i:= 0 to TSCriteria(Selected).ValueList.Count - 1 do
  begin
    v:= TSCriteria(Selected).ValueList.Items[i];
    frmSCValues.strgrdValue.Cells[0,i+1]:= IntToStr(i+1);
    frmSCValues.strgrdValue.Cells[1,i+1]:= v.Value;
    frmSCValues.strgrdValue.Cells[2,i+1]:= FloatToStr(v.Profit);    
  end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.RefreshValueConstraint;
begin
  SCMinValue:= TSCriteria(Selected).MinValue; // присваиваем компоненту TEdit значение
  SCMaxValue:= TSCriteria(Selected).MaxValue; // присваиваем компоненту TEdit значение
  frmSCCnvrsnPrmtr.cmbxFunction.ItemIndex:= TSCriteria(Selected).ConvFunc.ID;
  RefreshFuncTabSheet;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.ViewSimple;
var
  LastActivePage: TTabSheet;
begin
  LastActivePage:= pgcntrl.ActivePage;
  tbshtSCProperties.TabVisible:= TRUE;
  tbshtCCProperties.TabVisible:= FALSE;
  tbshtAOperator.TabVisible:= FALSE;
  // Устанавливаем значения критерия в окне просмотра его свойств
  edtSCriteriaName.Text:= Selected.Name;
  cmbxDataField.ItemIndex :=
    Max(cmbxDataField.Items.IndexOf(TSCriteria(Selected).DataField), 0);
  edtSWeight.Text:= FloatToStr(Selected.Weight);
  rchdtComment.Lines.Assign(Selected.Comment);
  cmbxFBehavior.ItemIndex:= TSCriteria(Selected).Behavior;
  // Отобразим необходимые элементы интерфейса, связанные с типом поведения
  OnBehaviorChange(Self);

  if (LastActivePage = tbshtConvFunction) and (not FDefaultPage) and
    (TSCriteria(Selected).Behavior <> 2) then
    pgcntrl.ActivePage:= tbshtConvFunction
  else if (LastActivePage = tbshtComment) and not FDefaultPage then
         pgcntrl.ActivePage:= tbshtComment
       else
       begin
         pgcntrl.ActivePage:= tbshtSCProperties;
         if Self.Visible then edtSCriteriaName.SetFocus;
       end;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.ViewComplex;
var
  LastActivePage: TTabSheet;
begin
  LastActivePage:= pgcntrl.ActivePage;
  tbshtSCProperties.TabVisible:= FALSE;
  tbshtConvFunction.TabVisible:= FALSE;
  tbshtCCProperties.TabVisible:= TRUE;
  case TCCriteria(Selected).Operator.ID of
    0,4: tbshtAOperator.TabVisible:= FALSE;
    else tbshtAOperator.TabVisible:= TRUE;
  end;
  // Устанавливаем значения критерия в окне просмотра его свойств
  edtCCriteriaName.Text:= Selected.Name;
  edtCWeight.Text:= FloatToStr(Selected.Weight);
  rchdtComment.Lines.Assign(Selected.Comment);
  cmbxAOperator.ItemIndex:= TCCriteria(Selected).Operator.ID;
  case cmbxAOperator.ItemIndex of
    0:
      begin // Аддитивный
        edtLambda.Text:= '';
        edtLambda.Enabled:= FALSE;
      end;
    1,2,3:
      begin // Степенной первого, второго, двойного типов
        edtLambda.Text:=
          FloatToStr(TPowerOpr(TCCriteria(Selected).Operator).Lambda);
        edtLambda.Enabled:= TRUE;
      end;
    4:
      begin // Мультипликативный
        edtLambda.Text:=
          FloatToStr(TMultiplicativeOpr(TCCriteria(Selected).Operator).Lambda);
        edtLambda.Enabled:= TRUE;
      end;
  end;
  // Устанавливаем пероначальный вид окна свойств
  EnableChangeProcessing;
  if (LastActivePage = tbshtComment) and not FDefaultPage then
  begin
    pgcntrl.ActivePage:= tbshtComment;
  end
  else if (LastActivePage = tbshtAOperator) and (not FDefaultPage)
         and tbshtAOperator.TabVisible then
       begin
         pgcntrl.ActivePage:= tbshtAOperator;
         tbshtAOperatorShow(Self);
       end
       else
       begin
         pgcntrl.ActivePage:= tbshtCCProperties;
         if Self.Visible then edtCCriteriaName.SetFocus;
       end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.RefreshView(Sender: TObject);
begin
  TCriteria(trvw.Selected.Data).AssignTo(FCurrentCriteria);
  // Отключаем обработку событий ориентированных на изменения со стороны
  // пользователя
  DisableChangeProcessing;
  if Selected is TSCriteria then // если единичный критерий
    ViewSimple;
  if Selected is TCCriteria then // если комплексный критерий
    ViewComplex;
  trvw.Enabled:= TRUE;
  btnApply.Enabled:= FALSE;
  btnCancel.Enabled:= FALSE;
  FDefaultPage:= FALSE;
  // Включаем обработку изменений
  EnableChangeProcessing;
end;

////////////////////////////////////////////////////////////////////////////////
// Выполняется в случае изменения свойств критерия
procedure TfrmCriteria.SetToChanged(Sender: TObject);
begin
  if FChangeProcessing then
  begin
    trvw.Enabled:= FALSE;
    btnApply.Enabled:= TRUE;
    btnCancel.Enabled:= TRUE;
    if Assigned(OnCriteriaChange) then
      OnCriteriaChange(Sender);
  end;
end;

////////////////////////////////////////////////////////////////////////////////
// Выполняется в случае применения изменений свойств критерия
procedure TfrmCriteria.ApplyChanges(Sender: TObject);
var
  PCriteria: ^TCriteria;
  OnProblemChange: TNotifyEvent;
begin
  // Проверим не пуст ли список допустимых значений в случае
  // качественного единичного критерия
  if Selected is TSCriteria then
    if TSCriteria(Selected).Behavior = 2 then
      if TSCriteria(Selected).ValueList.Count = 0 then
      begin
        MessageDlg('Не заполнен список допустимых значений'#13' единичного критерия',
          mtInformation, [mbOk], 0);
        Abort;
      end;
  PCriteria:= @TCriteria(trvw.Selected.Data);
  // Сохраним и отключим обработчик изменения задачи оценки
  OnProblemChange:= fMain.Problem.OnChange;
  fMain.Problem.OnChange:= nil;
  // Обновим критерий
  Selected.AssignTo(PCriteria^);
  // Востановим обработчик и если он назначен, то выполним его
  fMain.Problem.OnChange:= OnProblemChange;
  if Assigned(OnProblemChange) then  OnProblemChange(Self);

  if Assigned(OnApplyChanges) then  OnApplyChanges(Sender);
  RefreshView(Sender);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.CancelChanges(Sender: TObject);
begin
  if Assigned(OnCancelChanges) then OnCancelChanges(Sender);
  RefreshView(Sender);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnNodeSelection(Sender: TObject; Node: TTreeNode);
begin
  if Assigned(OnSelect) then OnSelect(Sender, Node);
  RefreshView(Sender);
end;

////////////////////////////////////////////////////////////////////////////////
// Метод выполняется при удаление критерия из дерева
procedure TfrmCriteria.OnNodeDeletion(Sender: TObject; Node: TTreeNode);
var
  Criteria: TCriteria;
begin
  Criteria:= Node.Data;
  Criteria.Free;
  FDefaultPage:= TRUE;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnNodeCollapsing(Sender: TObject; Node: TTreeNode;
  var AllowCollapse: Boolean);
begin
  AllowCollapse:= not (Node.Parent = nil);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnBehaviorChange(Sender: TObject);
begin
  case cmbxFBehavior.ItemIndex of
    2:
      begin
        frmSCCnvrsnPrmtr.Visible:= FALSE;
        tbshtConvFunction.TabVisible:= FALSE;
        frmSCValues.Visible:= TRUE;
        TSCriteria(Selected).Behavior:= 2;
        RefreshValueList;
      end;
   else
      begin
        frmSCValues.Visible:= FALSE;
        frmSCCnvrsnPrmtr.Visible:= TRUE;
        tbshtConvFunction.TabVisible:= TRUE;
        TSCriteria(Selected).Behavior:= cmbxFBehavior.ItemIndex;
        RefreshValueConstraint;
      end;
  end;
  SetToChanged(Sender);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.RepaintChart;
var
  f: TFunc;
  i: integer;
  x,y: double;
begin
  chrt.SeriesList[0].Clear;
  chrt.LeftAxis.Automatic:= FALSE;
  chrt.LeftAxis.AutomaticMaximum:= FALSE;
  chrt.LeftAxis.Maximum:= 1;
  chrt.LeftAxis.AutomaticMinimum:= FALSE;
  chrt.LeftAxis.Minimum:= 0;
  chrt.LeftAxis.Increment:= 0.1;

  f:= TSCriteria(Selected).ConvFunc;
  case cmbxFBehavior.ItemIndex of
    0: // возрастающее поведение
      for i:= 0 to 100 do
      begin
        x:= f.KMin + i/100*(f.KMax - f.KMin);
        y:= f.Calculate(x);
        chrt.SeriesList[0].AddXY(x,y);
      end;
    1: // убывающее поведение
      for i:= 0 to 100 do
      begin
        x:= f.KMin + i/100*(f.KMax - f.KMin);
        y:= f.Calculate(x);
        chrt.SeriesList[0].AddXY(x,1-y);
      end;
  end;
  // Далее добавление к графику специфических для функции точек
  if f is TBetaDistributionFN then
    chrt.SeriesList[0].AddXY(TBetaDistributionFN(f).Km,1 - cmbxFBehavior.ItemIndex);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.RefreshFuncTabSheet;
var
  f: TFunc;
  OldFChangeProcessing: Boolean;
begin
  f:= TSCriteria(Selected).ConvFunc;
  if f <> nil then
  begin
    case f.ID of
      0: // линейная функции перевода
        begin
          frmLinerFunc.Visible:= TRUE;
          frmBetaDistributionFunc.Visible:= FALSE;
          frmGaussianFunc.Visible:= FALSE;
          frmBetaFunc.Visible:= FALSE;
          frmExponentialFunc.Visible:= FALSE;

          OldFChangeProcessing:= FChangeProcessing;
          DisableChangeProcessing; // Необходимо чтобы не попасть в бесконечный цикл
          frmLinerFunc.edtKMin.Text:= FloatToStr(TLinerFN(f).KMin);
          frmLinerFunc.edtKMax.Text:= FloatToStr(TLinerFN(f).KMax);
          frmLinerFunc.edtLSatiation.Text:= FloatToStr(TLinerFN(f).LSatiation);
          frmLinerFunc.edtRSatiation.Text:= FloatToStr(TLinerFN(f).RSatiation);
          FChangeProcessing:= OldFChangeProcessing;
        end;
      1: // функция плотности бета-распределения
        begin
          frmLinerFunc.Visible:= FALSE;
          frmBetaDistributionFunc.Visible:= TRUE;
          frmGaussianFunc.Visible:= FALSE;
          frmBetaFunc.Visible:= FALSE;
          frmExponentialFunc.Visible:= FALSE;

          OldFChangeProcessing:= FChangeProcessing;
          DisableChangeProcessing; // Необходимо чтобы не попасть в бесконечный цикл
          frmBetaDistributionFunc.edtKMin.Text:=
            FloatToStr(TBetaDistributionFN(f).KMin);
          frmBetaDistributionFunc.edtKMax.Text:=
            FloatToStr(TBetaDistributionFN(f).KMax);
          frmBetaDistributionFunc.edtP.Text:=
            FloatToStr(TBetaDistributionFN(f).P);
          frmBetaDistributionFunc.edtQ.Text:=
            FloatToStr(TBetaDistributionFN(f).Q);
          frmBetaDistributionFunc.edtLSatiation.Text:=
            FloatToStr(TBetaDistributionFN(f).LSatiation);
          frmBetaDistributionFunc.edtRSatiation.Text:=
            FloatToStr(TBetaDistributionFN(f).RSatiation);
          FChangeProcessing:= OldFChangeProcessing;
        end;
      2: // гауссовская функции перевода
        begin
          frmLinerFunc.Visible:= FALSE;
          frmBetaDistributionFunc.Visible:= FALSE;
          frmGaussianFunc.Visible:= TRUE;
          frmBetaFunc.Visible:= FALSE;
          frmExponentialFunc.Visible:= FALSE;

          OldFChangeProcessing:= FChangeProcessing;
          DisableChangeProcessing; // Необходимо чтобы не попасть в бесконечный цикл
          frmGaussianFunc.edtKMin.Text:= FloatToStr(TLinerFN(f).KMin);
          frmGaussianFunc.edtKMax.Text:= FloatToStr(TLinerFN(f).KMax);
          frmGaussianFunc.edtLSatiation.Text:=
            FloatToStr(TGaussianFN(f).LSatiation);
          frmGaussianFunc.edtRSatiation.Text:=
            FloatToStr(TGaussianFN(f).RSatiation);
          FChangeProcessing:= OldFChangeProcessing;
        end;
      3: // бета функция
        begin
          frmLinerFunc.Visible:= FALSE;
          frmBetaDistributionFunc.Visible:= FALSE;
          frmGaussianFunc.Visible:= FALSE;
          frmBetaFunc.Visible:= TRUE;
          frmExponentialFunc.Visible:= FALSE;

          OldFChangeProcessing:= FChangeProcessing;
          DisableChangeProcessing; // Необходимо чтобы не попасть в бесконечный цикл
          frmBetaFunc.edtKMin.Text:= FloatToStr(TBetaFN(f).KMin);
          frmBetaFunc.edtKMax.Text:= FloatToStr(TBetaFN(f).KMax);
          frmBetaFunc.edtP.Text:= FloatToStr(TBetaFN(f).P);
          frmBetaFunc.edtQ.Text:= FloatToStr(TBetaFN(f).Q);
          frmBetaFunc.edtLSatiation.Text:= FloatToStr(TBetaFN(f).LSatiation);
          frmBetaFunc.edtRSatiation.Text:= FloatToStr(TBetaFN(f).RSatiation);
          FChangeProcessing:= OldFChangeProcessing;
        end;
      4: // показательная функция
        begin
          frmLinerFunc.Visible:= FALSE;
          frmBetaDistributionFunc.Visible:= FALSE;
          frmGaussianFunc.Visible:= FALSE;
          frmBetaFunc.Visible:= FALSE;
          frmExponentialFunc.Visible:= TRUE;

          OldFChangeProcessing:= FChangeProcessing;
          DisableChangeProcessing; // Необходимо чтобы не попасть в бесконечный цикл
          frmExponentialFunc.edtKMin.Text:= FloatToStr(TBetaFN(f).KMin);
          frmExponentialFunc.edtKMax.Text:= FloatToStr(TBetaFN(f).KMax);
          frmExponentialFunc.edtBase.Text:= FloatToStr(TExponentialFN(f).Base);
          frmExponentialFunc.edtLSatiation.Text:=
            FloatToStr(TExponentialFN(f).LSatiation);
          frmExponentialFunc.edtRSatiation.Text:=
            FloatToStr(TExponentialFN(f).RSatiation);
          FChangeProcessing:= OldFChangeProcessing;
        end;
    end;
    RepaintChart;
  end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnSCConvFunctionChange(Sender: TObject);
begin
  case frmSCCnvrsnPrmtr.cmbxFunction.ItemIndex of
    0: TSCriteria(Selected).ChangeToLiner;
    1: TSCriteria(Selected).ChangeToBetaDistribution;
    2: TSCriteria(Selected).ChangeToGaussian;
    3: TSCriteria(Selected).ChangeToBeta;
    4: TSCriteria(Selected).ChangeToExponential;
  end;
  SetToChanged(Sender);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnSCMinChange(Sender: TObject);
begin
  TSCriteria(Selected).MinValue:= SCMinValue;
  if FChangeProcessing then SetToChanged(Sender);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnSCMaxChange(Sender: TObject);
begin
  TSCriteria(Selected).MaxValue:= SCMaxValue;
  SetToChanged(Sender);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnFuncTabSheetShow(Sender: TObject);
begin
  RefreshFuncTabSheet;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnSCNameChange(Sender: TObject);
begin
  TSCriteria(Selected).Name:= edtSCriteriaName.Text;
  SetToChanged(Sender);
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnSCWeightChange(Sender: TObject);
begin
  try
    TSCriteria(Selected).Weight:= StrToFloat(edtSWeight.Text);
    SetToChanged(Sender);
  except
    on E: Exception do
    begin
      ShowMessage(E.Message);
      DisableChangeProcessing;
      edtSWeight.Text:= FloatToStr(TCriteria(Selected).Weight);
      EnableChangeProcessing;
    end;
  end;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnCCNameChange(Sender: TObject);
begin
  TSCriteria(Selected).Name:= edtCCriteriaName.Text;
  SetToChanged(Sender);
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnCCWeightChange(Sender: TObject);
begin
  try
    TSCriteria(Selected).Weight:= StrToFloat(edtCWeight.Text);
    SetToChanged(Sender);
  except
    on E: Exception do
    begin
      ShowMessage(E.Message);
      DisableChangeProcessing;
      edtCWeight.Text:= FloatToStr(TCriteria(Selected).Weight);
      EnableChangeProcessing;
    end;
  end;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnCommentChange(Sender: TObject);
begin
  SetToChanged(Sender);
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnCommentExit(Sender: TObject);
begin
  if btnApply.Enabled then
    Selected.Comment.Assign(rchdtComment.Lines);
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnSCPChange(Sender: TObject);
begin
  if FChangeProcessing then
    TBetaDistributionFN(TSCriteria(Selected).ConvFunc).P:=
      StrToFloat(frmBetaDistributionFunc.edtP.Text);
  RepaintChart;
  SetToChanged(Sender);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnSCQChange(Sender: TObject);
begin
  if FChangeProcessing then
    TBetaDistributionFN(TSCriteria(Selected).ConvFunc).Q:=
      StrToFloat(frmBetaDistributionFunc.edtQ.Text);
  RepaintChart;
  SetToChanged(Sender);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnCCOperatorChange(Sender: TObject);
begin
  case cmbxAOperator.ItemIndex of
    0:
    begin
      TCCriteria(Selected).ChangeToAdditive;
      edtLambda.Text:= '';
      edtLambda.Enabled:= FALSE;
    end;
    1: // Степенной первого типа
    begin
      if edtLambda.Text = '' then edtLambda.Text:= FloatToStr(0.5);
      edtLambda.Enabled:= TRUE;
      TCCriteria(Selected).ChangeToPowerI;
      TPowerIOpr(TCCriteria(Selected).Operator).Lambda:=
        StrToFloat(edtLambda.Text);
    end;
    2:// Степенной второго типа
    begin
      if edtLambda.Text = '' then edtLambda.Text:= FloatToStr(0.5);
      edtLambda.Enabled:= TRUE;
      TCCriteria(Selected).ChangeToPowerII;
      TPowerIIOpr(TCCriteria(Selected).Operator).Lambda:=
        StrToFloat(edtLambda.Text);
    end;
    3:// Двойной степенной
    begin
      if edtLambda.Text = '' then edtLambda.Text:= FloatToStr(0.5);
      edtLambda.Enabled:= TRUE;
      TCCriteria(Selected).ChangeToDoublePower;
      TDoublePowerOpr(TCCriteria(Selected).Operator).Lambda:=
        StrToFloat(edtLambda.Text);
    end;
    4:// Мультипликативный
    begin
      if edtLambda.Text = '' then edtLambda.Text:= FloatToStr(0.6);
      edtLambda.Enabled:= TRUE;
      TCCriteria(Selected).ChangeToMultiplicative;
      TMultiplicativeOpr(TCCriteria(Selected).Operator).Lambda:=
        StrToFloat(edtLambda.Text);
    end;
  end;
  case TCCriteria(Selected).Operator.ID of
    0,4: tbshtAOperator.TabVisible:= FALSE;
    else tbshtAOperator.TabVisible:= TRUE;
  end;
  SetToChanged(Sender);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.frmSCValuesstrgrdValueSelectCell(Sender: TObject;
  ACol, ARow: Integer; var CanSelect: Boolean);
var
  SValue: TSValue;
begin
  if TSCriteria(Selected).ValueList.Count > 0 then
  begin
     SValue:= TSCriteria(Selected).ValueList[(ARow - 1)];
     frmSCValues.StrgrdValue.Hint:= FloatToStr(SValue.StdProfit);
  end
  else frmSCValues.StrgrdValue.Hint:= '';
  CanSelect:= TRUE;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.AddSValue;
var
  dlg: TdlgSCValue;
  NewValue: string;
  NewProfit: Double;
  NoError: boolean;
begin
  NoError:= TRUE;
  dlg:= nil;
  try
    dlg:= TdlgSCValue.Create(Self);
    dlg.Caption:= 'Новое значение';
    repeat
      if dlg.ShowModal = mrOK then
        try
          NewValue:= dlg.edtValue.Text;
          NewProfit:= StrToFloat(dlg.edtProfit.Text);
          TSCriteria(Selected).ValueList.Add(NewValue, NewProfit);
          NoError:= TRUE;
        except
          on E: Exception do
          begin
            MessageDlg(E.Message ,mtInformation,[mbOK],0);
            NoError:= FALSE;
          end;
        end
       else NoError:= TRUE;
     until NoError;
  finally
    dlg.Free;
  end;
  RefreshValueList;
  SetToChanged(Self);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.UpdateSValue;
var
  dlg: TdlgSCValue;
  NewValue: string;
  NewProfit: Double;
  NoError: boolean;
  SValue: TSValue;
begin
  NoError:= TRUE;
  if TSCriteria(Selected).ValueList.Count > 0 then
  begin
  dlg:= nil;
  try
    dlg:= TdlgSCValue.Create(Self);
    dlg.Caption:= 'Изменение значения';
    SValue:=
      TSCriteria(Selected).ValueList.Items[frmSCValues.strgrdValue.Row - 1];
    dlg.edtValue.Text:= SValue.Value;
    dlg.edtProfit.Text:= FloatToStr(SValue.Profit);
    repeat
      if dlg.ShowModal = mrOK then
        try
          NewValue:= dlg.edtValue.Text;
          NewProfit:= StrToFloat(dlg.edtProfit.Text);
          TSCriteria(Selected).ValueList.Update(frmSCValues.strgrdValue.Row
            - 1,NewValue, NewProfit);
          NoError:= TRUE;
        except
          on E: Exception do
          begin
            MessageDlg(E.Message ,mtInformation,[mbOK],0);
            NoError:= FALSE;
          end;
        end
      else NoError:= TRUE;
    until NoError;
  finally
    dlg.Free;
  end;
  RefreshValueList;
  SetToChanged(Self);
  end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.DeleteSValue;
begin
  if TSCriteria(Selected).ValueList.Count > 0 then
  begin
    TSCriteria(Selected).ValueList.Delete(frmSCValues.strgrdValue.Row - 1);
    RefreshValueList;
    SetToChanged(Self);
  end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnStrgrdValueKeyDown(Sender: TObject;
  var Key: Word; Shift: TShiftState);
begin
  case Key of
  VK_INSERT: AddSValue; // Добавление значения
  VK_DELETE: DeleteSValue;// Удаление значения из списка допустимых значений
  VK_RETURN: UpdateSValue; // Изменнение параметров имеющегося значения
  end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.frmSCValuesstrgrdValueDblClick(Sender: TObject);
begin
  UpdateSValue;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.cmbxDataFieldChange(Sender: TObject);
begin
  if cmbxDataField.ItemIndex <> 0 then
    TSCriteria(Selected).DataField:= cmbxDataField.Text
  else TSCriteria(Selected).DataField:= '';
  SetToChanged(Self);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.frmSCValuesbtnFillClick(Sender: TObject);
begin
  if (fMain.frmObject.cnctnData.Connected) and
    (TSCriteria(Selected).DataField <> '') then
  begin
    fMain.frmObject.qry.SQL.Clear;
    fMain.frmObject.qry.SQL.Add('select distinct ' +
      TSCriteria(Selected).DataField + ' from ' +
      fMain.frmObject.tblData.TableName);
    TSCriteria(Selected).ValueList.Clear;
    fMain.frmObject.qry.Open;
    while not fMain.frmObject.qry.Eof do
    begin
      TSCriteria(Selected).ValueList.Add(
        AnsiUpperCase(fMain.frmObject.qry.Fields[0].AsString),1);
      fMain.frmObject.qry.Next;
    end;
    fMain.frmObject.qry.Close;
    RefreshValueList;
    SetToChanged(Self);
  end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.OnCCLambdaChange(Sender: TObject);
begin
  try
    case TCCriteria(Selected).Operator.ID of
      1,2,3: TPowerOpr(TCCriteria(Selected).Operator).Lambda:=
        StrToFloat(edtLambda.Text);
      4: TMultiplicativeOpr(TCCriteria(Selected).Operator).Lambda:=
        StrToFloat(edtLambda.Text);
    end;
  except
    MessageDlg('Недопустимое значение коэффицента жесткости', mtInformation,
      [mbOk], 0);
  end;
  SetToChanged(Sender);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.tbshtAOperatorShow(Sender: TObject);
var
  op: TOperator;
  i: Integer;
  x,y: Double;
begin
  chrtAOperator.SeriesList[0].Clear;
  chrtAOperator.SeriesList[1].Clear;
  op:= TCCriteria(Selected).Operator;
  case op.ID of
  1: // Степенной I типа
    begin
      chrtAOperator.SeriesList[1].AddXY(0,TPowerIOpr(op).CalculateG(0));
      chrtAOperator.SeriesList[1].AddXY(1,TPowerIOpr(op).CalculateG(1));
      for i:= 0 to 50 do
      begin
        x:= i/50;
        y:= TPowerIOpr(op).CalculateF(x);
        chrtAOperator.SeriesList[0].AddXY(x,y);
      end;
    end;
    2: // Степенной II типа
    begin
      chrtAOperator.SeriesList[0].AddXY(0,TPowerIIOpr(op).CalculateF(0));
      chrtAOperator.SeriesList[0].AddXY(1,TPowerIIOpr(op).CalculateF(1));
      for i:= 0 to 50 do
      begin
        x:= i/50;
        y:= TPowerIIOpr(op).CalculateG(x);
        chrtAOperator.SeriesList[1].AddXY(x,y);
      end;
    end;
    3: // Двойной степенной
    begin
      for i:= 0 to 50 do
      begin
        x:= i/50;
        y:= TDoublePowerOpr(op).CalculateF(x);
        chrtAOperator.SeriesList[0].AddXY(x,y);
        y:= TDoublePowerOpr(op).CalculateG(x);
        chrtAOperator.SeriesList[1].AddXY(x,y);
      end;
    end;
  end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmCriteria.frmBetaFuncedtPChange(Sender: TObject);
begin
  if FChangeProcessing then
    TBetaFN(TSCriteria(Selected).ConvFunc).P:=
      StrToFloat(frmBetaFunc.edtP.Text);
  RepaintChart;
  SetToChanged(Sender);
end;

procedure TfrmCriteria.frmBetaFuncedtQChange(Sender: TObject);
begin
  if FChangeProcessing then
    TBetaFN(TSCriteria(Selected).ConvFunc).Q:=
      StrToFloat(frmBetaFunc.edtQ.Text);
  RepaintChart;
  SetToChanged(Sender);
end;

procedure TfrmCriteria.frmExponentialFuncedtBaseChange(Sender: TObject);
begin
  if FChangeProcessing then
    TExponentialFN(TSCriteria(Selected).ConvFunc).Base:=
      StrToFloat(frmExponentialFunc.edtBase.Text);
  RepaintChart;
  SetToChanged(Sender);
end;

procedure TfrmCriteria.frmBetaDistributionFuncedtLSatiationChange(
  Sender: TObject);
begin
  if FChangeProcessing then
    TBetaDistributionFN(TSCriteria(Selected).ConvFunc).LSatiation:=
      StrToFloat(frmBetaDistributionFunc.edtLSatiation.Text);
  RepaintChart;
  SetToChanged(Sender);
end;

procedure TfrmCriteria.frmBetaDistributionFuncedtRSatiationChange(
  Sender: TObject);
begin
  if FChangeProcessing then
    TBetaDistributionFN(TSCriteria(Selected).ConvFunc).RSatiation:=
      StrToFloat(frmBetaDistributionFunc.edtRSatiation.Text);
  RepaintChart;
  SetToChanged(Sender);
end;

procedure TfrmCriteria.frmBetaFuncedtLSatiationChange(Sender: TObject);
begin
  if FChangeProcessing then
    TBetaFN(TSCriteria(Selected).ConvFunc).LSatiation:=
      StrToFloat(frmBetaFunc.edtLSatiation.Text);
  RepaintChart;
  SetToChanged(Sender);
end;

procedure TfrmCriteria.frmBetaFuncedtRSatiationChange(Sender: TObject);
begin
  if FChangeProcessing then
    TBetaFN(TSCriteria(Selected).ConvFunc).RSatiation:=
      StrToFloat(frmBetaFunc.edtRSatiation.Text);
  RepaintChart;
  SetToChanged(Sender);
end;

procedure TfrmCriteria.frmLinerFuncedtLSatiationChange(Sender: TObject);
begin
  if FChangeProcessing then
    TLinerFN(TSCriteria(Selected).ConvFunc).LSatiation:=
      StrToFloat(frmLinerFunc.edtLSatiation.Text);
  RepaintChart;
  SetToChanged(Sender);
end;

procedure TfrmCriteria.frmLinerFuncedtRSatiationChange(Sender: TObject);
begin
  if FChangeProcessing then
    TLinerFN(TSCriteria(Selected).ConvFunc).RSatiation:=
      StrToFloat(frmLinerFunc.edtRSatiation.Text);
  RepaintChart;
  SetToChanged(Sender);
end;

procedure TfrmCriteria.frmGaussianFuncedtLSatiationChange(Sender: TObject);
begin
  if FChangeProcessing then
    TGaussianFN(TSCriteria(Selected).ConvFunc).LSatiation:=
      StrToFloat(frmGaussianFunc.edtLSatiation.Text);
  RepaintChart;
  SetToChanged(Sender);
end;

procedure TfrmCriteria.frmGaussianFuncedtRSatiationChange(Sender: TObject);
begin
  if FChangeProcessing then
    TGaussianFN(TSCriteria(Selected).ConvFunc).RSatiation:=
      StrToFloat(frmGaussianFunc.edtRSatiation.Text);
  RepaintChart;
  SetToChanged(Sender);
end;

procedure TfrmCriteria.frmExponentialFuncedtLSatiationChange(
  Sender: TObject);
begin
  if FChangeProcessing then
    TExponentialFN(TSCriteria(Selected).ConvFunc).LSatiation:=
      StrToFloat(frmExponentialFunc.edtLSatiation.Text);
  RepaintChart;
  SetToChanged(Sender);
end;

procedure TfrmCriteria.frmExponentialFuncedtRSatiationChange(
  Sender: TObject);
begin
  if FChangeProcessing then
    TExponentialFN(TSCriteria(Selected).ConvFunc).RSatiation:=
      StrToFloat(frmExponentialFunc.edtRSatiation.Text);
  RepaintChart;
  SetToChanged(Sender);
end;

end.
