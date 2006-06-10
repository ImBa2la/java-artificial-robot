unit LinerFuncFrm;

interface

uses 
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  StdCtrls, ComCtrls;

type
  TfrmLinerFunc = class(TFrame)
    lblKMin: TLabel;
    lblKMax: TLabel;
    edtKMin: TEdit;
    edtKMax: TEdit;
    lblLSatiationl: TLabel;
    edtLSatiation: TEdit;
    UpDownLSatiation: TUpDown;
    lblRSatiationl: TLabel;
    edtRSatiation: TEdit;
    UpDownUpDownRSatiation: TUpDown;
    procedure UpDownLSatiationChangingEx(Sender: TObject;
      var AllowChange: Boolean; NewValue: Smallint;
      Direction: TUpDownDirection);
    procedure UpDownRSatiationChangingEx(Sender: TObject;
      var AllowChange: Boolean; NewValue: Smallint;
      Direction: TUpDownDirection);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

implementation

{$R *.DFM}
uses math;

procedure TfrmLinerFunc.UpDownLSatiationChangingEx(Sender: TObject;
  var AllowChange: Boolean; NewValue: Smallint;
  Direction: TUpDownDirection);
begin
  case Direction of
    updUp: edtLSatiation.Text:=
      FloatToStr(Min(StrToFloat(edtLSatiation.Text) + 0.1, 1));
    updDown: edtLSatiation.Text:=
      FloatToStr(Max(StrToFloat(edtLSatiation.Text) - 0.1, 0));
  end;
   AllowChange:= FALSE;
end;

procedure TfrmLinerFunc.UpDownRSatiationChangingEx(Sender: TObject;
  var AllowChange: Boolean; NewValue: Smallint;
  Direction: TUpDownDirection);
begin
  case Direction of
    updUp: edtRSatiation.Text:=
      FloatToStr(Min(StrToFloat(edtRSatiation.Text) + 0.1, 1));
    updDown: edtRSatiation.Text:=
      FloatToStr(Max(StrToFloat(edtRSatiation.Text) - 0.1, 0));
  end;
   AllowChange:= FALSE;
end;

end.
