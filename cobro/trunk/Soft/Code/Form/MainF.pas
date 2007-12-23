unit MainF;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  ComCtrls, ToolWin, ExtCtrls, Menus, ActnList, StdCtrls, ObjectFrm,
  CriteriaFrm, Db, Grids, DBGrids, ADODB, ProblemDM, ImgList;

const
  ProgramName = 'Multicriterion Problem Decision';

type
  TProgramState = ( psProblem, psCriteria, psObject);

  TfMain = class(TForm)
    MainMenu: TMainMenu;
    mmiFile: TMenuItem;
    mmiCriteria: TMenuItem;
    mmiAddCriteria: TMenuItem;
    mmiDeleteCriteria: TMenuItem;
    mmiExit: TMenuItem;
    mmiDevider1: TMenuItem;
    mmiCreate: TMenuItem;
    mmiOpen: TMenuItem;
    mmiSaveAs: TMenuItem;
    mmiObject: TMenuItem;
    mmiEvalute: TMenuItem;
    mmiViewCriteria: TMenuItem;
    mmiViewObject: TMenuItem;
    mmiClose: TMenuItem;
    mmiView: TMenuItem;
    mmiSimple: TMenuItem;
    mmiComplex: TMenuItem;
    mmiSave: TMenuItem;
    mmiDevider2: TMenuItem;
    mmiProperties: TMenuItem;
    stsbr: TStatusBar;
    cbr: TControlBar;
    tlbMainMenu: TToolBar;
    tlbtnFile: TToolButton;
    tlbtnView: TToolButton;
    tlbtnCriteria: TToolButton;
    tlbtnObject: TToolButton;
    frmObject: TfrmObject;
    aclMain: TActionList;
    actExit: TAction;
    actViewCriteria: TAction;
    actViewData: TAction;
    actClose: TAction;
    actSave: TAction;
    actCreate: TAction;
    actOpen: TAction;
    actViewProperties: TAction;
    actSaveAs: TAction;
    tlbr: TToolBar;
    ToolButton1: TToolButton;
    ToolButton2: TToolButton;
    actVievResult: TAction;
    imglstTool: TImageList;
    tlbrCriteriaManipulation: TToolBar;
    tlbtnAddSCriteria: TToolButton;
    tlbtnDeleteCriteria: TToolButton;
    tlbtnAddCCriteria: TToolButton;
    actAddSCriteria: TAction;
    actAddCCriteria: TAction;
    actDeleteCriteria: TAction;
    tlbrBasic: TToolBar;
    tlbtnOpen: TToolButton;
    ToolButton4: TToolButton;
    tlbtn: TToolButton;
    pmnCriteriaManipulation: TPopupMenu;
    pmmiAddCriteria: TMenuItem;
    pmmiDeleteCriteria: TMenuItem;
    pmmiAddSCriteria: TMenuItem;
    pmmiAddCCriteria: TMenuItem;
    frmCriteria: TfrmCriteria;
    procedure OnFormCreate(Sender: TObject);
    procedure OnFormClose(Sender: TObject; var Action: TCloseAction);

    procedure actCreateExecute(Sender: TObject);
    procedure actOpenExecute(Sender: TObject);
    procedure actSaveExecute(Sender: TObject);
    procedure actSaveAsExecute(Sender: TObject);
    procedure actCloseExecute(Sender: TObject);
    procedure actExitExecute(Sender: TObject);
    procedure actViewCriteriaExecute(Sender: TObject);
    procedure actViewDataExecute(Sender: TObject);
    procedure actViewPropertiesExecute(Sender: TObject);
    procedure OnSelectNewCriteria(Sender: TObject; Node: TTreeNode);
    procedure OnCriteriaChange(Sender: TObject);
    procedure OnApplyChanges(Sender: TObject);
    procedure OnCancelChanges(Sender: TObject);
    procedure actVievResultExecute(Sender: TObject);
    procedure actAddSCriteriaExecute(Sender: TObject);
    procedure actAddCCriteriaExecute(Sender: TObject);
    procedure actDeleteCriteriaExecute(Sender: TObject);
  private
    FState: TProgramState;
  protected
    procedure SetState(Value: TProgramState);
    procedure UpdateCaption();
  public
    Problem: TdmProblem;
    procedure RefreshView(Sender: TObject);
    // �������� ��������� ���������
    // psProblem - ������ �� �������, �� ���������
    // psCriteria - � ������ �������������� ���������
    // psObject - � ������ �������������� ������
    property State: TProgramState read FState write SetState;
  end;

var
  fMain: TfMain;

implementation

uses ProblemPropertiesF, Criteria, CCriteria, ResultF, SCriteria,
  DependenceF;

{$R *.DFM}
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.OnFormCreate(Sender: TObject);
begin
  Caption:= ProgramName;
  FState:= psProblem;
  RefreshView( Self );
  frmCriteria.OnCriteriaChange:= Self.OnCriteriaChange;
  frmCriteria.OnApplyChanges:= Self.OnApplyChanges;
  frmCriteria.OnCancelChanges:= Self.OnCancelChanges;
  frmCriteria.OnSelect:= Self.OnSelectNewCriteria;  
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.OnFormClose(Sender: TObject; var Action: TCloseAction);
begin
  try
    if Assigned( Problem ) then
      case Problem.Close() of
      mrOk: // ������ ������� ������� � ����������� ��� ���
        Action:= caFree;
      mrCancel: // ������ �������� ������
        Action:= caNone;
      mrAbort: // ��� ���������� � �������� �������� ��������� ������
        Abort;
      end
    else Action:= caFree;
  except
    case MessageDlg('���������� ��������� ������!'#13'����� ��� ����������?',
      mtConfirmation, [mbYes, mbNo], 0) of
    mrYes:
      begin
          Problem.Free;
          Action:= caFree;
      end;
    mrNo: Action:= caNone;
    end;
  end;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.OnSelectNewCriteria(Sender: TObject; Node: TTreeNode);
var IsComplexCriteria: Boolean;
begin
  actDeleteCriteria.Enabled:= Node.Parent <> nil;
  IsComplexCriteria:= TCriteria(Node.Data) is TCCriteria;
  mmiAddCriteria.Enabled:= IsComplexCriteria;
  pmmiAddCriteria.Enabled:= IsComplexCriteria;
  actAddSCriteria.Enabled:= IsComplexCriteria;
  actAddCCriteria.Enabled:= IsComplexCriteria;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.OnCriteriaChange(Sender: TObject);
begin
  // ������� ���� �����������
  cbr.Enabled:= FALSE;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.OnApplyChanges(Sender: TObject);
begin
  // ������� ���� ���������
  cbr.Enabled:= TRUE;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.OnCancelChanges(Sender: TObject);
begin
  // ������� ���� ���������
  cbr.Enabled:= TRUE;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.UpdateCaption();
begin
  case FState of
  psProblem:
    Caption:= ProgramName;
  psCriteria:
    Caption:= ProgramName + ' - ' + Problem.Name + ' [������ ���������]';
  psObject:
    Caption:= ProgramName + ' - ' + Problem.Name + ' [������� ������]'
  end;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.RefreshView(Sender: TObject);
begin
  // �������� ������� ��� �������� ���� � ������������ � ����������,
  // �������� ���������� �������
  case FState of
  psProblem:
    begin
      frmObject.Visible:= FALSE;
      actViewData.Checked:= FALSE;
      actViewData.Enabled:= FALSE;
      frmCriteria.Visible:= FALSE;
      actViewCriteria.Checked:= FALSE;
      actViewCriteria.Enabled:= FALSE;
      // ��� ������� ���� ����������� � ToolButton ���������� ���������� �
      // ����� ToolButton.
      mmiObject.Enabled:= FALSE;
      tlbtnObject.Enabled:= FALSE;
      mmiCriteria.Enabled:= FALSE;
      tlbtnCriteria.Enabled:= FALSE;

      actSave.Enabled:= FALSE;
      actSaveAs.Enabled:= FALSE;
      actClose.Enabled:= FALSE;
      tlbtnView.Enabled:= FALSE;
      mmiView.Enabled:= FALSE;

      actDeleteCriteria.Enabled:= FALSE;
      mmiAddCriteria.Enabled:= FALSE;
      actAddSCriteria.Enabled:= FALSE;
      actAddCCriteria.Enabled:= FALSE;
    end;
  psObject:
    begin
      frmCriteria.Visible:= FALSE;
      actViewCriteria.Checked:= FALSE;
      actViewCriteria.Enabled:= TRUE;
      actViewData.Enabled:= TRUE;
      frmObject.Visible:= TRUE;
      actViewData.Checked:= TRUE;
      // ��� ������� ���� ����������� � ToolButton ���������� ���������� �
      // ����� ToolButton.
      mmiCriteria.Enabled:= FALSE;
      tlbtnCriteria.Enabled:= FALSE;
      actDeleteCriteria.Enabled:= FALSE;
      mmiAddCriteria.Enabled:= FALSE;
      actAddSCriteria.Enabled:= FALSE;
      actAddCCriteria.Enabled:= FALSE;

      mmiObject.Enabled:= TRUE;
      tlbtnObject.Enabled:= TRUE;

      actSave.Enabled:= Problem.Changed;
      actSaveAs.Enabled:= TRUE;
      actClose.Enabled:= TRUE;
      tlbtnView.Enabled:= TRUE;
      mmiView.Enabled:= TRUE;
    end;
  psCriteria:
    begin
      frmObject.Visible:= FALSE;
      actViewData.Checked:= FALSE;
      frmCriteria.Visible:= TRUE;
      actViewCriteria.Checked:= TRUE;
      actViewCriteria.Enabled:= TRUE;
      actViewData.Enabled:= TRUE;
      // ��� ������� ���� ����������� � ToolButton ���������� ���������� �
      // ����� ToolButton.
      mmiObject.Enabled:= FALSE;
      tlbtnObject.Enabled:= FALSE;
      mmiCriteria.Enabled:= TRUE;
      tlbtnCriteria.Enabled:= TRUE;

  actDeleteCriteria.Enabled:= frmCriteria.trvw.Selected.Parent <> nil;
  mmiAddCriteria.Enabled:= TCriteria(frmCriteria.Selected) is TCCriteria;
  actAddSCriteria.Enabled:= TCriteria(frmCriteria.Selected) is TCCriteria;
  actAddCCriteria.Enabled:= TCriteria(frmCriteria.Selected) is TCCriteria;

      actSave.Enabled:= Problem.Changed;
      actSaveAs.Enabled:= TRUE;
      actClose.Enabled:= TRUE;
      tlbtnView.Enabled:= TRUE;
      mmiView.Enabled:= TRUE;
      frmCriteria.RefreshView(Self);
    end;
  end;
  UpdateCaption();
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.SetState(Value: TProgramState);
begin
  case Value of
  psProblem:
     case Problem.Close() of
     mrOk: // ������ ������� ������� � ����������� ��� ���
       begin
           Problem:= nil;
           FState:= Value;
           RefreshView(Self);
       end;
     mrCancel: // ������ �������� ������
       begin
       end;
     mrAbort: // ��� ���������� � �������� �������� ��������� ������
       Abort;
     end;
  else
    FState:= Value;
    RefreshView(Self);
  end;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.actCreateExecute(Sender: TObject);
begin
  // � ����������� �� ��������� ��������� ������� �� ��� ���� �������
  case FState of
  psProblem: // ������� ������
    begin
      Problem:= TdmProblem.Create(Self, frmCriteria.trvw.Items, '����� ������');
      Problem.OnChange:= Self.RefreshView;
      Problem.OnAddCriteria:= frmCriteria.OnAddCriteria;
      // ��-��������� ��� ������ ���������
      State:= psCriteria;
    end;
  end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TfMain.actOpenExecute(Sender: TObject);
begin
  // � ����������� �� ��������� ��������� ��������� �� ��� ���� �������
  case FState of
  psObject: // �������� ������ ��� ������
    frmObject.LoadData;
  psProblem: // �������� ������ �� �����
    begin
      // ������� ����������� ��������� ������ �� �����
      Problem:= TdmProblem.Create( Self , frmCriteria.trvw.Items);
      Problem.OnChange:= Self.RefreshView;
      Problem.OnAddCriteria:= frmCriteria.OnAddCriteria;
      State:= psCriteria;
    end;
  end;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.actSaveExecute(Sender: TObject);
begin
  Problem.Save;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.actSaveAsExecute(Sender: TObject);
begin
  Problem.SaveAs;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.actCloseExecute(Sender: TObject);
begin
  case State of
  psObject:
    begin
      frmObject.cnctnData.Connected:= FALSE;
    end;
  else
    State:= psProblem;
  end;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.actExitExecute(Sender: TObject);
begin
  Close;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.actViewCriteriaExecute(Sender: TObject);
begin
  if not actViewCriteria.Checked then State:= psCriteria;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.actViewDataExecute(Sender: TObject);
begin
  if not actViewData.Checked then State:= psObject;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.actViewPropertiesExecute(Sender: TObject);
begin
  try
    fProblemProperties:= TfProblemProperties.Create( Self );
    fProblemProperties.edtProblemName.Text:= Problem.Name;
    fProblemProperties.edtAuthor.Text:= Problem.Author;
    fProblemProperties.mDescription.Lines.Assign( Problem.Comment );
    fProblemProperties.ShowModal;
  finally
    fProblemProperties.Free;
  end;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TfMain.actVievResultExecute(Sender: TObject);
var
  fResult: TfResult;
  i,j: Integer;
  Criteria: TCriteria;
  Node: TTreeNode;
  p: TTreeNode;
  s: string;
  DataSet: TDataSet;
  ObjectNameField: string;
begin
  fResult:= TfResult.Create(Self);
  try
    with fResult.grdResult do
    begin
      DataSet:= frmObject.tblData;
      if DataSet.Active then
      begin
        if DataSet.RecordCount = 0 then Abort;
        ColCount:= DataSet.RecordCount*2 + 3;
      end
      else Abort;
      RowCount:= frmCriteria.trvw.Items.Count + 1;
      // ��������� ���������� � ���������
      for i:= 0 to frmCriteria.trvw.Items.Count - 1 do
      begin
        Node:= frmCriteria.trvw.Items[i];
        Criteria:= Node.Data;
        s:= IntTostr(Node.Index + 1);
        p:= Node.Parent;
        while p <> nil do
        begin
          s:= IntToStr(p.Index + 1) + '.' + s;
          p:= p.Parent;
        end;
        Cells[0, i + 1]:= s +' ' + Criteria.Name;
{TODO        Floats[1, i + 1]:= Criteria.Weight;
        Floats[2, i + 1]:= Criteria.StdWeight; }
      end;
      // ��������� ���������� �� �������� ������
      DataSet.DisableControls;
      try
        DataSet.First; j:=1;
        ObjectNameField:= frmObject.cbxNameField.Text;
        while not DataSet.Eof do
        begin
          for i:= 0 to frmCriteria.trvw.Items.Count - 1 do
          begin
            Node:= frmCriteria.trvw.Items[i];
            Criteria:= Node.Data;
            Cells[j*2+1,0]:= DataSet.FieldByName(ObjectNameField).AsString;
            Cells[j*2+2,0]:= '��';
            if Criteria is TSCriteria then
              Cells[j*2+1,i + 1]:=
                DataSet.FieldByName(TSCriteria(Criteria).DataField).AsString;
//TODO            Floats[j*2+2, i + 1]:= Criteria.Value;
          end;
          DataSet.Next;  j:= j+1;
        end;
      finally
        DataSet.EnableControls;
      end;

      AutoSize:= TRUE;
    end;
    fResult.ShowModal;
  finally
    fResult.Free;
  end;
end;

procedure TfMain.actAddSCriteriaExecute(Sender: TObject);
begin
  Problem.AddCriteria(frmCriteria.trvw.Selected, ctSimple);
end;

procedure TfMain.actAddCCriteriaExecute(Sender: TObject);
begin
  Problem.AddCriteria(frmCriteria.trvw.Selected, ctComplex);
end;

procedure TfMain.actDeleteCriteriaExecute(Sender: TObject);
begin
  Problem.DeleteCriteria(frmCriteria.trvw.Selected);
end;

end.
