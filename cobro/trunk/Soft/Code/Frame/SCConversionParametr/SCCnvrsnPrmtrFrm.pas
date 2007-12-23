unit SCCnvrsnPrmtrFrm;

interface

uses 
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  StdCtrls;

type
  TfrmSCCnvrsnPrmtr = class(TFrame)
    edtSMin: TEdit;
    lblMin: TLabel;
    lblSCFunction: TLabel;
    cmbxFunction: TComboBox;
    lblMax: TLabel;
    edtSMax: TEdit;
  private
    { Private declarations }
  public
    { Public declarations }
  end;

implementation

{$R *.DFM}

end.
