unit ObjectFrm;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  StdCtrls, Db, ADODB, Grids, DBGrids, DBTables, ExtCtrls;

type
  TfrmObject = class(TFrame)
    opndlgObject: TOpenDialog;
    cnctnData: TADOConnection;
    tblData: TADOTable;
    dsData: TDataSource;
    dbgrdData: TDBGrid;
    qry: TADOQuery;
    pnl: TPanel;
    lblObjectNameField: TLabel;
    cbxNameField: TComboBox;
    Button1: TButton;
    procedure tblDataAfterOpen(DataSet: TDataSet);
    procedure tblDataAfterClose(DataSet: TDataSet);
    procedure cbxNameFieldChange(Sender: TObject);
    procedure Button1Click(Sender: TObject);
  public
    procedure LoadData;
  end;

implementation

uses MainF, CriteriaFrm, SCriteria, Criteria;

{$R *.DFM}

////////////////////////////////////////////////////////////////////////////////
procedure TfrmObject.tblDataAfterOpen(DataSet: TDataSet);
var
  i: integer;
  p: TCriteria;
begin
  for i:= 0 to DataSet.Fields.Count - 1 do
  begin
    fMain.frmCriteria.cmbxDataField.Items.Add(DataSet.Fields.Fields[i].DisplayName);
    cbxNameField.Items.Add(DataSet.Fields.Fields[i].DisplayName);
  end;
  // ���� ���� ���� ������ � ��� �� ������, � ������� ��� ���� ������� ��������,
  // �� ���������� �������� � ���
  for i:= 0 to fMain.frmCriteria.trvw.Items.Count - 1 do
  begin
    p:= fMain.frmCriteria.trvw.Items[i].Data;
    if (p is TSCriteria) then
      if cbxNameField.Items.IndexOf(TSCriteria(p).OldDataField) > -1 then
        TSCriteria(p).DataField := TSCriteria(p).OldDataField
      else TSCriteria(p).DataField := '';
  end;

  cbxNameField.ItemIndex:= 0;
  if Assigned(fMain.Problem) then  fMain.Problem.Changed:= TRUE;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfrmObject.tblDataAfterClose(DataSet: TDataSet);
var
  i: integer;
  p: TCriteria;
begin
  for i:= 0 to fMain.frmCriteria.trvw.Items.Count - 1 do
  begin
    p:= fMain.frmCriteria.trvw.Items[i].Data;
    if (p is TSCriteria) then
    begin
      // �������� ���� ������ ��� �������� � ����� ������ ��� �� ���������
      // ��� �������� � ��������� ���
      TSCriteria(p).OldDataField := TSCriteria(p).DataField;
      // ������� �������� �������� � ����������� ���� ������
      TSCriteria(p).DataField := '';
    end;
  end;
  with fMain.frmCriteria.cmbxDataField.Items do
  begin
    Clear;
    Add('�� ���������');
  end;
  cbxNameField.Items.Clear;
  if  Assigned(fMain.Problem) then
    fMain.Problem.Changed:= TRUE;
end;

////////////////////////////////////////////////////////////////////////////////
//TODO: ������� �������� ������ �� ������ ���������� ������������� � ������������
procedure TfrmObject.LoadData;
const
  dBaseStr: string = 'Provider=MSDASQL.1;Persist Security Info=False;Data Source=dBASE Files;Initial Catalog=%0:s';
  VisualFoxProStr: string ='Provider=MSDASQL.1;Persist Security Info=False;Extended Properties="DSN=Visual FoxPro Tables;UID=;SourceDB=%0:s;SourceType=DBF;Exclusive=No;BackgroundFetch=Yes;Collate=Machine;Null=Yes;Deleted=Yes;"';
var
  Path: string;
begin
  opndlgObject.InitialDir:= GetCurrentDir;
  if opndlgObject.Execute then
  begin
    // ������� ����� ������ ���� �� ������
    if cnctnData.Connected then cnctnData.Connected:= FALSE;

    Path:= ExtractFileDir(opndlgObject.FileName);
    case opndlgObject.FilterIndex of
      1: cnctnData.ConnectionString:= Format(dBaseStr, [Path]);
      2: cnctnData.ConnectionString:= Format(VisualFoxProStr, [Path]);
    end;

    // ������� ����� ����� ������
    cnctnData.Connected:= TRUE;
    tblData.TableName:= ChangeFileExt(ExtractFileName(opndlgObject.FileName),'');
    try
      tblData.Active:= TRUE;
    except
      cnctnData.Connected:= FALSE; // ��������� ����������
      MessageDlg('��������� ������ �������� ���'#13
        + '�� ������������ ���������� ����', mtInformation, [mbOK], 0);
    end;
  end;
end;

procedure TfrmObject.cbxNameFieldChange(Sender: TObject);
begin
  if  Assigned(fMain.Problem) then
    fMain.Problem.Changed:= TRUE;
end;

procedure SaveDataSetAsCSVFile(ds: TDataSet; FileName: string);
var
  F: TextFile;
  s: string;
  i: Integer;
begin
  if ds.Active then
  begin
    AssignFile(F, ChangeFileExt(FileName, '.csv'));
    Rewrite(F);
    // ������� ��������� �������
    s := '';
    for i := 0 to ds.FieldCount - 1 do
      s := s + ds.FieldDefs[i].DisplayName + ';';
    Delete(s, Length(s), 1);
    WriteLn(F, s);
    // ������� ������
    ds.DisableControls;
    ds.First;
    while not ds.EOF do
    begin
      s := '';
      for i := 0 to ds.FieldCount - 1 do
        s := s + ds.Fields[i].AsString + ';';
      Delete(s, Length(s), 1);
      WriteLn(F, s);

      ds.Next;
    end;
    ds.EnableControls;
    CloseFile(F);
  end;
end;

procedure TfrmObject.Button1Click(Sender: TObject);
begin
  SaveDataSetAsCSVFile(tblData, 'c:\test.txt');
end;

end.
