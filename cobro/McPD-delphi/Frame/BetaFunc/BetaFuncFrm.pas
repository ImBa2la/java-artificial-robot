unit BetaFuncFrm;

interface

uses 
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  ComCtrls, StdCtrls;

type
  TfrmBetaFunc = class(TFrame)
    lblKMin: TLabel;
    edtKMin: TEdit;
    lblP: TLabel;
    edtP: TEdit;
    edtQ: TEdit;
    edtKMax: TEdit;
    lblKMax: TLabel;
    lblQ: TLabel;
    UpDownP: TUpDown;
    UpDownQ: TUpDown;
    lblLSatiationl: TLabel;
    edtLSatiation: TEdit;
    UpDownLSatiation: TUpDown;
    UpDownUpDownRSatiation: TUpDown;
    edtRSatiation: TEdit;
    lblRSatiationl: TLabel;
    procedure UpDownPChangingEx(Sender: TObject; var AllowChange: Boolean;
      NewValue: Smallint; Direction: TUpDownDirection);
    procedure UpDownQChangingEx(Sender: TObject; var AllowChange: Boolean;
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

procedure TfrmBetaFunc.UpDownPChangingEx(Sender: TObject;
  var AllowChange: Boolean; NewValue: Smallint;
  Direction: TUpDownDirection);
begin
  case Direction of
    updUp: edtP.Text:= FloatToStr(Min(StrToFloat(edtP.Text) + 0.1, 100));
    updDown: edtP.Text:= FloatToStr(Max(StrToFloat(edtP.Text) - 0.1, 0));
  end;
   AllowChange:= FALSE;
end;

procedure TfrmBetaFunc.UpDownQChangingEx(Sender: TObject;
  var AllowChange: Boolean; NewValue: Smallint;
  Direction: TUpDownDirection);
begin
  case Direction of
    updUp: edtQ.Text:= FloatToStr(Min(StrToFloat(edtQ.Text) + 0.1, 100));
    updDown: edtQ.Text:= FloatToStr(Max(StrToFloat(edtQ.Text) - 0.1, 0));
  end;
   AllowChange:= FALSE;
end;

procedure TfrmBetaFunc.UpDownLSatiationChangingEx(Sender: TObject;
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

procedure TfrmBetaFunc.UpDownRSatiationChangingEx(Sender: TObject;
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
