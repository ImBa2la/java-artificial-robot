///////////////////////////////////////////////////////////////////////////////
// ������ �������� ����� ����������� ����������� ��������
unit CCriteria;

interface

uses Criteria, comctrls, Operator;

type

  TCCriteria = class( TCriteria )
  private
    FOperator: TOperator;
  protected
    function GetValue: double; override;
    function GetAbsValue: double; override;
  public
    constructor Create(AOwner: TTreeNode; AName: string; AWeight: double = 1);
    procedure AssignTo(var Distination: TCriteria); override;
    procedure ChangeToAdditive;
    procedure ChangeToPowerI;
    procedure ChangeToPowerII;
    procedure ChangeToDoublePower;
    procedure ChangeToMultiplicative;
    function GetBaseOfStandartization: double;
    procedure OnChildWeightChange(Sender: TCriteria);
    property Operator: TOperator read FOperator;
  end;

implementation

uses Classes;
////////////////////////////////////////////////////////////////////////////////
procedure TCCriteria.ChangeToAdditive;
begin
  FOperator.Free;
  FOperator:= TAdditiveOpr.Create(Self);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TCCriteria.ChangeToPowerI;
begin
  FOperator.Free;
  FOperator:= TPowerIOpr.Create(Self);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TCCriteria.ChangeToPowerII;
begin
  FOperator.Free;
  FOperator:= TPowerIIOpr.Create(Self);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TCCriteria.ChangeToDoublePower;
begin
  FOperator.Free;
  FOperator:= TDoublePowerOpr.Create(Self);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TCCriteria.ChangeToMultiplicative;
begin
  FOperator.Free;
  FOperator:= TMultiplicativeOpr.Create(Self);
end;

////////////////////////////////////////////////////////////////////////////////
constructor TCCriteria.Create(AOwner: TTreeNode; AName: string;
  AWeight: double = 1);
begin
  inherited Create(AOwner, AName, AWeight);
  if AOwner is TTreeNode then
  begin
    AOwner.ImageIndex:= 0;
    AOwner.SelectedIndex:= 0;
  end;
  // ��-��������� ���������� �������� �������������
  FOperator:= TAdditiveOpr.Create(Self);
end;
////////////////////////////////////////////////////////////////////////////////
procedure TCCriteria.AssignTo(var Distination: TCriteria);
var
  OldOwner: TTreeNode;
  OldOnWeightChange: TCriteriaEvent;
  OldOnChange: TNotifyEvent;
begin
  if Assigned(Distination) then
    begin
      OldOwner:= Distination.Owner;
      OldOnWeightChange:= Distination.OnWeightChange;
      OldOnChange:= Distination.OnChange;
      Distination.Free;
    end
  else
    begin
      OldOwner:= nil;
      OldOnWeightChange:= nil;
      OldOnChange:= nil;
    end;
  Distination:= TCCriteria.Create(OldOwner, Name, Weight);
  
  // ���������� ��� ���������� ������������������ ���������
  Distination.BaseCriteria:= Self; //!!!!!!!!!!!!!!!!!!!!!!

  Operator.AssignTo( TCCriteria(Distination).FOperator );
  Distination.Comment.Assign(Comment);

  Distination.OnWeightChange:= OldOnWeightChange;
  if Assigned(Distination.OnWeightChange) then
    Distination.OnWeightChange(Distination);
  Distination.OnChange:= OldOnChange;
  if Assigned(Distination.OnChange) then
    Distination.OnChange(Distination);
end;

////////////////////////////////////////////////////////////////////////////////
function TCCriteria.GetBaseOfStandartization: double;
var
  ChildCriteria: TCriteria;
begin
  Result:= 0;
  ChildCriteria:= GetFistChild;
  while ChildCriteria <> nil do
  begin
    Result:= Result + ChildCriteria.Weight;
    ChildCriteria:= GetNextChild(ChildCriteria);
  end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TCCriteria.OnChildWeightChange(Sender: TCriteria);
var
  ChildCriteria: TCriteria;
  Base: double;
begin
  // ���������� ����������� ���� � ���� ������������ ���������
  Base:= 0;
  ChildCriteria:= GetFistChild;
  if Assigned(ChildCriteria) then Base:= GetBaseOfStandartization;
  while ChildCriteria <> nil do
  begin
    ChildCriteria.StandartizeWeight(Base);
    ChildCriteria:= GetNextChild(ChildCriteria);
  end;
  // ���� ������������ ����������������� ��������,
  // �� ���������� ����������� ���������
  if Operator is TMultiplicativeOpr then
    TMultiplicativeOpr(Operator).RecalculateC;
end;

////////////////////////////////////////////////////////////////////////////////
function TCCriteria.GetValue: double;
begin
  Result:= FOperator.Calculate;
end;

////////////////////////////////////////////////////////////////////////////////
function TCCriteria.GetAbsValue: double;
begin
  Result:= FOperator.Calculate;
end;

////////////////////////////////////////////////////////////////////////////////
end.
