unit ResultF;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  Grids, StdCtrls, ComCtrls, ToolWin, ImgList;

type
  TfResult = class(TForm)
    grdResult: TStringGrid;
    tlb: TToolBar;
    tlbtnSave: TToolButton;
    imglst: TImageList;
    svdlg: TSaveDialog;
    ToolButton1: TToolButton;
    procedure tlbtnSaveClick(Sender: TObject);
    procedure ToolButton1Click(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

implementation

uses DependenceF, Options, Math;

{$R *.DFM}

procedure TfResult.tlbtnSaveClick(Sender: TObject);
begin
  svdlg.InitialDir:= GetCurrentDir;
//  if svdlg.Execute then
//  begin
//   grdResult.SaveToXLS( 'result.xls' );
//   ChangeFileExt(ExtractFileName(svdlg.FileName),'.xls')
//  end;
end;

procedure TfResult.ToolButton1Click(Sender: TObject);
var
  i: integer;
begin
  fDependence:= TfDependence.Create(Self);
  try
    // ��������� �������� ���������
    for i:= 1 to grdResult.RowCount - 1 do
    begin
      fDependence.cmbxV.Items.Add(grdResult.Cells[0,i]);
      fDependence.cmbxH.Items.Add(grdResult.Cells[0,i]);
    end;
    // ������������� ��������� � ������� ��� �������� � �� ��.���������
    fDependence.cmbxV.ItemIndex :=
      Min(VerticalDependenceCriteria, fDependence.cmbxV.Items.Count - 1);
    fDependence.cmbxVMeasureUnit.ItemIndex := VerticalDependenceCriteriaUnit;
    fDependence.UpdateVMesureUnit;

    fDependence.cmbxH.ItemIndex :=
      Min(HorizontalDependenceCriteria, fDependence.cmbxV.Items.Count - 1);
    fDependence.cmbxHMeasureUnit.ItemIndex := HorizontalDependenceCriteriaUnit;
    fDependence.UpdateHMesureUnit;
    // ���������� �����
    fDependence.ShowModal;
    // ���������� ��������� �������� � �� ��.���������
    VerticalDependenceCriteria := fDependence.cmbxV.ItemIndex;
    HorizontalDependenceCriteria := fDependence.cmbxH.ItemIndex;
    VerticalDependenceCriteriaUnit := fDependence.cmbxVMeasureUnit.ItemIndex;
    HorizontalDependenceCriteriaUnit := fDependence.cmbxHMeasureUnit.ItemIndex;
  finally
    fDependence.Free;
    fDependence := nil;
  end;
end;

end.