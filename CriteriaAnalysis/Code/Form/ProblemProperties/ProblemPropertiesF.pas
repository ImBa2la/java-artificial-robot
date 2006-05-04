unit ProblemPropertiesF;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  StdCtrls, Buttons, ComCtrls;

type
  TfProblemProperties = class(TForm)
    btbtnCancel: TBitBtn;
    btbtnApply: TBitBtn;
    btbtnOK: TBitBtn;
    pgcntrl: TPageControl;
    tbshtCommon: TTabSheet;
    lblProblemName: TLabel;
    edtProblemName: TEdit;
    lblAuthor: TLabel;
    edtAuthor: TEdit;
    lblDescription: TLabel;
    mDescription: TRichEdit;
    procedure btbtnApplyClick(Sender: TObject);
    procedure btbtnOKClick(Sender: TObject);
    procedure EnableApplyBtn(Sender: TObject);
    procedure FormShow(Sender: TObject);
    procedure mDescriptionKeyDown(Sender: TObject; var Key: Word;
      Shift: TShiftState);
  private
    procedure SaveChanges;
  public
    { Public declarations }
  end;

var
  fProblemProperties: TfProblemProperties;

implementation

uses MainF;

{$R *.DFM}
procedure TfProblemProperties.SaveChanges;
begin
  with fMain.Problem do
  begin
      Name:= edtProblemName.Text;
      Author:= edtAuthor.Text;
      Comment.Assign( mDescription.Lines );
  end;
end;

procedure TfProblemProperties.btbtnApplyClick(Sender: TObject);
begin
  SaveChanges;
  btbtnApply.Enabled:= FALSE;
end;

procedure TfProblemProperties.btbtnOKClick(Sender: TObject);
begin
  if btbtnApply.Enabled then SaveChanges;
end;

procedure TfProblemProperties.EnableApplyBtn(Sender: TObject);
begin
  btbtnApply.Enabled:= TRUE;
end;

procedure TfProblemProperties.FormShow(Sender: TObject);
begin
  btbtnApply.Enabled:= FALSE;
  pgcntrl.ActivePage:= tbshtCommon;
  edtProblemName.SetFocus;
end;

procedure TfProblemProperties.mDescriptionKeyDown(Sender: TObject;
  var Key: Word; Shift: TShiftState);
begin
  if Key = VK_ESCAPE then ModalResult:= mrCancel;
end;

end.
