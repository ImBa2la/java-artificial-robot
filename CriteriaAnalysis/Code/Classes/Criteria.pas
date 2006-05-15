unit Criteria;

interface

uses comctrls, classes;

type
  TCriteria = class;

  TCriteriaEvent = procedure (Sender: TCriteria) of object;

  TCriteria = class
  private
    FOwner: TTreeNode;
    FName: string;
    FWeight: double;
    FStdWeight: double; // ������������� ��� ��������
    FComment: TStringList;
    FOnWeightChange: TCriteriaEvent;
    FOnChange: TNotifyEvent;
  protected
    procedure SetName(AName: string);
    procedure SetWeight(AWeight: double);
    function GetValue: double; virtual; abstract;
    function GetAbsValue: double; virtual; abstract;
  public
    BaseCriteria: TCriteria;
    constructor Create(AOwner: TTreeNode; AName: string; AWeight: double = 1);
    destructor Destroy; override;
    procedure Assign(Sourse: TCriteria); virtual;
    procedure AssignTo(var Distination: TCriteria); virtual; abstract;
    function GetFistChild: TCriteria;
    function GetNextChild(ACriteria: TCriteria): TCriteria;
    procedure StandartizeWeight(ABase: double);
    property OnWeightChange: TCriteriaEvent read FOnWeightChange write
      FOnWeightChange;
    property OnChange: TNotifyEvent read FOnChange write FOnChange;
    property Owner: TTreeNode read FOwner;
    property Name: string read FName write SetName;
    property Weight: double read FWeight write SetWeight;
    property StdWeight: double read FStdWeight;
    property Value: double read GetValue;
    property AbsValue: double read GetAbsValue;
    property Comment: TStringList read FComment;
  end;

implementation

uses SysUtils;
////////////////////////////////////////////////////////////////////////////////
constructor TCriteria.Create(AOwner: TTreeNode; AName: string;
  AWeight: double = 1);
begin
  inherited Create;
  FOwner:= AOwner;
  if FOwner is TTreeNode then FOwner.Data:= Self;
  Name:= AName;
  FWeight:= AWeight;
  FStdWeight:= 1;
  FComment:= TStringList.Create;
end;

////////////////////////////////////////////////////////////////////////////////
destructor TCriteria.Destroy;
begin
  if Assigned( FOnChange ) then FOnChange( Self );
  FComment.Free;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TCriteria.Assign(Sourse: TCriteria);
begin
  Name:= Sourse.Name;
  Weight:= Sourse.Weight;
  Comment.Assign(Sourse.Comment);
  OnWeightChange:= Sourse.OnWeightChange;
  OnChange:= Sourse.OnChange;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TCriteria.SetName(AName: string);
begin
  FName:= AName;
  if FOwner is TTreeNode then FOwner.Text:= FName;
  //if Assigned( FOnChange ) then FOnChange( Self );
end;

////////////////////////////////////////////////////////////////////////////////
procedure TCriteria.SetWeight(AWeight: double);
begin
  if AWeight > 0 then
  begin
    FWeight:= AWeight;
    if Assigned(FOnWeightChange) then FOnWeightChange(Self);
   // if Assigned( FOnChange ) then FOnChange( Self );
  end
  else
     raise Exception.Create('��� �������� ������ ���� ������ ����');
end;

////////////////////////////////////////////////////////////////////////////////
// ����� ���������� ������ �������� ��������
function TCriteria.GetFistChild: TCriteria;
var
  ChildNode: TTreeNode;
begin
  Result:= nil;
  if Assigned(Owner) then
  begin
    ChildNode:= Owner.GetFirstChild;
    if ChildNode <> nil then
      Result:= ChildNode.Data;
  end;
end;

////////////////////////////////////////////////////////////////////////////////
// ����� ���������� ��������� �������� ��������.
// ���� �������� ���, �� ������������ nil
function TCriteria.GetNextChild(ACriteria: TCriteria): TCriteria;
var
  NextChildNode: TTreeNode;
  ParentNode: TTreeNode;
begin
  Result:= nil;
  // �������� ���� �� � ������� �������� TTreeNode
  if Assigned(ACriteria.Owner) then
  begin
    // ������� ����-�������� ��������� ������
    ParentNode:= ACriteria.Owner.Parent;
    if Assigned(ParentNode) then
    begin
      // ������ ��� ����-�������� ������ ���� ��������� �� �����, �������
      // ������� ��������
      NextChildNode:= ParentNode.GetNextChild(ACriteria.Owner);
      // ���� ������������ ��������� ����, �� ��������� ������-��������
      if NextChildNode <> nil then
        Result:= NextChildNode.Data;
    end;
  end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TCriteria.StandartizeWeight(ABase: double);
begin
  if FWeight <= ABase then
    FStdWeight:= FWeight/ABase
  else
    raise Exception.Create('�������� ������������� �����������');
end;

////////////////////////////////////////////////////////////////////////////////
{function TCriteria.GetValue: double;
begin
  Result:= 0;
end;}

////////////////////////////////////////////////////////////////////////////////
end.