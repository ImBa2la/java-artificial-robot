unit SCValueDlg;

interface

uses Windows, SysUtils, Classes, Graphics, Forms, Controls, StdCtrls, 
  Buttons, ExtCtrls;

type
  TdlgSCValue = class(TForm)
    OKBtn: TButton;
    CancelBtn: TButton;
    bvl: TBevel;
    edtValue: TEdit;
    edtProfit: TEdit;
    lblValue: TLabel;
    lblProfit: TLabel;
    procedure FormShow(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  dlgSCValue: TdlgSCValue;

implementation

{$R *.DFM}

procedure TdlgSCValue.FormShow(Sender: TObject);
begin
  edtValue.SetFocus;
end;

end.
