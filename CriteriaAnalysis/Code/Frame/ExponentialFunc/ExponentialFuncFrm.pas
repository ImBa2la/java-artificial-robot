unit ExponentialFuncFrm;

interface

uses 
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  ComCtrls, StdCtrls;

type
  TfrmExponentialFunc = class(TFrame)
    lblKMin: TLabel;
    edtKMin: TEdit;
    lblBase: TLabel;
    edtBase: TEdit;
    UpDownP: TUpDown;
    lblKMax: TLabel;
    edtKMax: TEdit;
    lblLSatiationl: TLabel;
    edtLSatiation: TEdit;
    UpDownLSatiation: TUpDown;
    lblRSatiationl: TLabel;
    edtRSatiation: TEdit;
    UpDownUpDownRSatiation: TUpDown;
    procedure UpDownPChangingEx(Sender: TObject; var AllowChange: Boolean;
      NewValue: Smallint; Direction: TUpDownDirection);
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

procedure TfrmExponentialFunc.UpDownPChangingEx(Sender: TObject;
  var AllowChange: Boolean; NewValue: Smallint;
  Direction: TUpDownDirection);
begin
  case Direction of
    updUp: edtBase.Text:= FloatToStr(Min(StrToFloat(edtBase.Text) + 0.01, 100));
    updDown: edtBase.Text:= FloatToStr(Max(StrToFloat(edtBase.Text) - 0.01, 0));
  end;
   AllowChange:= FALSE;
end;

procedure TfrmExponentialFunc.UpDownLSatiationChangingEx(Sender: TObject;
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

procedure TfrmExponentialFunc.UpDownRSatiationChangingEx(
  Sender: TObject; var AllowChange: Boolean; NewValue: Smallint;
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
