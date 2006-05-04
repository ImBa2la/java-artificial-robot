unit DependenceF;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  StdCtrls, TeeProcs, TeEngine, Chart, ExtCtrls, Series, ComCtrls;

type
  TfDependence = class(TForm)
    pnl: TPanel;
    chrt: TChart;
    cmbxV: TComboBox;
    cmbxH: TComboBox;
    lblV: TLabel;
    lblH: TLabel;
    Series1: TPointSeries;
    StatusBar: TStatusBar;
    cmbxVMeasureUnit: TComboBox;
    lblVMeasureUnit: TLabel;
    lblHMeasureUnit: TLabel;
    cmbxHMeasureUnit: TComboBox;
    procedure chrtMouseMove(Sender: TObject; Shift: TShiftState; X,
      Y: Integer);
    procedure FormCreate(Sender: TObject);
    procedure FormDestroy(Sender: TObject);
    procedure FormShow(Sender: TObject);
    procedure cmbxVMeasureUnitChange(Sender: TObject);
    procedure cmbxVChange(Sender: TObject);
    procedure cmbxHChange(Sender: TObject);
  private
    FObjectName: TStringList;
  public
    procedure UpdateVMesureUnit;
    procedure UpdateHMesureUnit;
    procedure RefreshChart(VCriteriaIndex, HCriteriaIndex: Integer);
  end;

var
  fDependence: TfDependence;

implementation

uses MainF, Criteria, DB, SCriteria;

{$R *.DFM}

procedure TfDependence.UpdateVMesureUnit;
var
  Criteria: TCriteria;
begin
  Criteria:= fMain.frmCriteria.trvw.Items[cmbxV.ItemIndex].Data;
  if Criteria is TSCriteria then
  begin
    if TSCriteria(Criteria).Behavior <> 2 then
      cmbxVMeasureUnit.Enabled:= TRUE
    else
    begin
      cmbxVMeasureUnit.Enabled:= FALSE;
      cmbxVMeasureUnit.ItemIndex:= 0;
    end;
  end
  else
  begin
    cmbxVMeasureUnit.Enabled:= FALSE;
    cmbxVMeasureUnit.ItemIndex:= 0;
  end;
end;

procedure TfDependence.UpdateHMesureUnit;
var
  Criteria: TCriteria;
begin
  Criteria:= fMain.frmCriteria.trvw.Items[cmbxH.ItemIndex].Data;
  if Criteria is TSCriteria then
  begin
    if TSCriteria(Criteria).Behavior <> 2 then
      cmbxHMeasureUnit.Enabled:= TRUE
    else
    begin
      cmbxHMeasureUnit.Enabled:= FALSE;
      cmbxHMeasureUnit.ItemIndex:= 0;
    end;
  end
  else
  begin
    cmbxHMeasureUnit.Enabled:= FALSE;
    cmbxHMeasureUnit.ItemIndex:= 0;
  end;
end;

procedure TfDependence.RefreshChart(VCriteriaIndex, HCriteriaIndex: Integer);
var
  HCriteria: TCriteria;
  VCriteria: TCriteria;
  DataSet: TDataSet;
  ObjectNameField: string;
  i: Longint;
  x,y: Double;
begin
  DataSet:= fMain.frmObject.tblData;
  // проверим открыт ли набор данных и есть ли хоть один объект оценки
  if DataSet.Active then
  begin
    if DataSet.RecordCount = 0 then Abort;
  end
  else Abort;
  // Индекс критериев не должен привышать допустимое значение
  if (VCriteriaIndex > fMain.frmCriteria.trvw.Items.Count - 1) or
    (HCriteriaIndex > fMain.frmCriteria.trvw.Items.Count - 1) then Abort;

  VCriteria:= fMain.frmCriteria.trvw.Items[VCriteriaIndex].Data;
  HCriteria:= fMain.frmCriteria.trvw.Items[HCriteriaIndex].Data;

  ObjectNameField:= fMain.frmObject.cbxNameField.Text;
  chrt.Series[0].Clear;
  FObjectName.Clear;
  // Заполняем график
  x := 0; y := 0;
  DataSet.DisableControls;
  DataSet.First;
  while not DataSet.Eof do
  begin
    case cmbxVMeasureUnit.ItemIndex of
      0: y := VCriteria.Value; // относительные ед.измерения
      1: y := VCriteria.AbsValue; // натуральные ед.измерения
    end;
    case cmbxHMeasureUnit.ItemIndex of
      0: x := HCriteria.Value; // относительные ед.измерения
      1: x := HCriteria.AbsValue;  // натуральные ед.измерения
    end;
    chrt.Series[0].AddXY(x, y, DataSet.FieldByName(ObjectNameField).AsString);

    DataSet.Next;
  end;
  DataSet.EnableControls;
  // Запоминаем соответствующие узлам имена объектов
  for i:= 0 to chrt.Series[0].Count - 1 do
  begin
    FObjectName.Add(chrt.Series[0].XLabel[i]);
    chrt.Series[0].XLabel[i] := '';
  end;

end;

procedure TfDependence.chrtMouseMove(Sender: TObject; Shift: TShiftState;
  X, Y: Integer);
var
  ValueIndex: Integer;
begin
  ValueIndex:= chrt.Series[0].GetCursorValueIndex;
  if ValueIndex = -1 then
    StatusBar.SimpleText:= ''
  else
  begin
    StatusBar.SimpleText:=
      '[' + FObjectName.Strings[ValueIndex]
      + '] : [' + Copy(cmbxV.Text, Pos(' ', cmbxV.Text)+1, Length(cmbxV.Text)-Pos(' ', cmbxV.Text))
      + ' - ' + FloatToStrF(chrt.Series[0].YValue[ValueIndex], ffFixed, 4, 4)
      + '] - [' + Copy(cmbxH.Text, Pos(' ', cmbxH.Text)+1, Length(cmbxH.Text)-Pos(' ', cmbxH.Text))
      + ' - ' + FloatToStrF(chrt.Series[0].XValue[ValueIndex], ffFixed, 4, 4) + ']';
  end;
end;

procedure TfDependence.FormCreate(Sender: TObject);
begin
  FObjectName:= TStringList.Create;
end;

procedure TfDependence.FormDestroy(Sender: TObject);
begin
  FObjectName.Free;
end;

procedure TfDependence.FormShow(Sender: TObject);
begin
  RefreshChart(cmbxV.ItemIndex, cmbxH.ItemIndex);
end;

procedure TfDependence.cmbxVMeasureUnitChange(Sender: TObject);
begin
  RefreshChart(cmbxV.ItemIndex, cmbxH.ItemIndex);
end;

procedure TfDependence.cmbxVChange(Sender: TObject);
begin
  UpdateVMesureUnit;
  RefreshChart(cmbxV.ItemIndex, cmbxH.ItemIndex);
end;

procedure TfDependence.cmbxHChange(Sender: TObject);
begin
  UpdateHMesureUnit;
  RefreshChart(cmbxV.ItemIndex, cmbxH.ItemIndex);
end;

end.
