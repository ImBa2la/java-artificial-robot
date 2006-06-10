unit SCValuesFrm;

interface

uses 
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  StdCtrls, Grids, ExtCtrls;

type
  TfrmSCValues = class(TFrame)
    strgrdValue: TStringGrid;
    pnl: TPanel;
    btnFill: TButton;
    Label1: TLabel;
  private
    { Private declarations }
  public
    constructor Create(AOwner: TComponent); override;
  end;

implementation

{$R *.DFM}
constructor TfrmSCValues.Create(AOwner: TComponent);
begin
  inherited Create(AOwner);
  strgrdValue.Cells[1,0]:= 'Значение';
  strgrdValue.Cells[2,0]:= 'Полезность';
end;

end.
