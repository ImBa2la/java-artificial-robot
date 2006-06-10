unit BetaDistributionFuncFrm;

interface

uses 
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  StdCtrls, ComCtrls;

type
  TfrmBetaDistributionFunc = class(TFrame)
    lblKMin: TLabel;
    edtKMin: TEdit;
    lblKMax: TLabel;
    edtKMax: TEdit;
    edtP: TEdit;
    UpDownP: TUpDown;
    edtQ: TEdit;
    UpDownQ: TUpDown;
    lblQ: TLabel;
    lblP: TLabel;
    edtLSatiation: TEdit;
    lblLSatiationl: TLabel;
    UpDownLSatiation: TUpDown;
    lblRSatiationl: TLabel;
    edtRSatiation: TEdit;
    UpDownUpDownRSatiation: TUpDown;
    procedure UpDownPChangingEx(Sender: TObject; var AllowChange: Boolean;
      NewValue: Smallint; Direction: TUpDownDirection);
    procedure UpDownQChangingEx(Sender: TObject; var AllowChange: Boolean;
      NewValue: Smallint; Direction: TUpDownDirection);
    procedure UpDownLSatiationChangingEx(Sender: TObject;
      var AllowChange: Boolean; NewValue: Smallint;
      Direction: TUpDownDirection);
    procedure UpDownUpDownRSatiationChangingEx(Sender: TObject;
      var AllowChange: Boolean; NewValue: Smallint;
      Direction: TUpDownDirection);
  end;

implementation

{$R *.DFM}
uses math;

procedure TfrmBetaDistributionFunc.UpDownPChangingEx(Sender: TObject;
  var AllowChange: Boolean; NewValue: Smallint;
  Direction: TUpDownDirection);
begin
  case Direction of
    updUp: edtP.Text:= FloatToStr(Min(StrToFloat(edtP.Text) + 0.1, 100));
    updDown: edtP.Text:= FloatToStr(Max(StrToFloat(edtP.Text) - 0.1, 0));
  end;
   AllowChange:= FALSE;
end;

procedure TfrmBetaDistributionFunc.UpDownQChangingEx(Sender: TObject;
  var AllowChange: Boolean; NewValue: Smallint;
  Direction: TUpDownDirection);
begin
  case Direction of
    updUp: edtQ.Text:= FloatToStr(Min(StrToFloat(edtQ.Text) + 0.1, 100));
    updDown: edtQ.Text:= FloatToStr(Max(StrToFloat(edtQ.Text) - 0.1, 0));
  end;
   AllowChange:= FALSE;
end;

procedure TfrmBetaDistributionFunc.UpDownLSatiationChangingEx(
  Sender: TObject; var AllowChange: Boolean; NewValue: Smallint;
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

procedure TfrmBetaDistributionFunc.UpDownUpDownRSatiationChangingEx(
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
