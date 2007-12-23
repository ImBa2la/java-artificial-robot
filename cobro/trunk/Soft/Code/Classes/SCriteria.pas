///////////////////////////////////////////////////////////////////////////////
// ������ �������� ����� ����������� ��������� ��������
unit SCriteria;

interface
uses Criteria, comctrls, ConvFunc, Classes;

type

  TSValue = class
    FValue: string;
    FProfit: Double;
    FStdValue: Double;
  protected
    procedure SetProfit(AProfit: double);
  public
    property Value: string read FValue write FValue;
    property Profit: Double read FProfit write SetProfit;
    property StdProfit: Double read FStdValue write FStdValue;
    constructor Create(AValue: string; AProfit: Double);
  end;

  TSValueList = class (TList)
  public
    procedure Standardize;
    function Add(AValue: string; AProfit: Double): Integer;
    procedure Delete(Index: Integer);
    destructor Destroy; override;
    procedure Update(ItemIndex: Integer; AValue: string; AProfit: Double);
    procedure AssignTo(var Distination: TSValueList);
  end;

  TSCriteria = class( TCriteria )
  private
    // ��� ��������� ��������
    // 0-������������ 1-��������� 2-������������
    FBehavior: word;
    FMin: double;
    FMax: double;
    FConvFunc: TFunc;
    FValueList: TSValueList;
    FDataField: string;
    FOldDataField: string;
  protected
    procedure SetBehavior(ABehavior: word); virtual;
    procedure SetMinValue(AValue: double);
    procedure SetMaxValue(AValue: double);
    function GetValue: double; override;
    function GetAbsValue: double; override;
  public
    constructor Create(AOwner: TTreeNode; AName: string; AWeight: double = 1;
      ABehavior: word = 0; AMin: double = 0; AMax: double = 1);
    destructor Destroy; override;
    procedure Assign(Sourse: TCriteria); override;
    procedure AssignTo(var Distination: TCriteria); override;
    procedure ChangeToLiner;
    procedure ChangeToBetaDistribution;
    procedure ChangeToBeta;
    procedure ChangeToGaussian;
    procedure ChangeToExponential;
    property OldDataField: string read FOldDataField write FOldDataField;
    property DataField: string read FDataField write FDataField;
    property Behavior: word read FBehavior write SetBehavior;
    property MinValue: double read FMin write SetMinValue;
    property MaxValue: double read FMax write SetMaxValue;
    property ConvFunc: TFunc read FConvFunc;
    property ValueList: TSValueList read FValueList;
  end;

implementation

uses math, Sysutils, Dialogs, MainF;

////////////////////////////////////////////////////////////////////////////////
constructor TSValue.Create(AValue: string; AProfit: Double);
begin
  Profit:= AProfit;
  Value:= AValue;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TSValue.SetProfit(AProfit: double);
begin
  if (AProfit > 0) and (AProfit <= 100) then
    FProfit:= AProfit
  else raise Exception.Create('������������ �������� ����������');
end;

////////////////////////////////////////////////////////////////////////////////
procedure TSValueList.Standardize;
var
  i: integer;
  MaxProfit: double;
begin
  MaxProfit:= 0;
  for i:= 0 to Count - 1 do
    MaxProfit:= Max(MaxProfit, TSValue(Items[i]).Profit);
  for i:= 0 to Count - 1 do
    TSValue(Items[i]).StdProfit:= TSValue(Items[i]).Profit / MaxProfit;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TSValueList.Delete(Index: Integer);
begin
  inherited Delete(Index);
  Standardize; 
end;

////////////////////////////////////////////////////////////////////////////////
function TSValueList.Add(AValue: string; AProfit: Double): Integer;
var i: integer;
begin
  for i:= 0 to Count - 1 do
    if TSValue(Items[i]).Value = AValue then
      raise Exception.Create('������������ �������� ��������');
  Result:= inherited Add( TSValue.Create(AValue, AProfit) );
  Standardize;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TSValueList.Update(ItemIndex: Integer; AValue: string; AProfit: Double);
var
  v: TSValue;
  i: integer;
begin
  for i:= 0 to Count - 1 do
    if (TSValue(Items[i]).Value = AValue) and (i <> ItemIndex) then
      raise Exception.Create('������������ �������� ��������');
  v:= TSValue(Items[ItemIndex]);
  v.Value:= AValue;
  v.Profit:= AProfit;
  Standardize;
end;

////////////////////////////////////////////////////////////////////////////////
destructor TSValueList.Destroy;
var i: integer;
begin
  for i:= 0 to Count - 1 do
    TObject(Items[i]).Free;
  inherited Destroy;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TSValueList.AssignTo(var Distination: TSValueList);
var i: Integer;
begin
  Distination.Free;
  Distination:= TSValueList.Create;
  for i:= 0 to Count - 1 do
    Distination.Add(TSValue(Items[i]).Value, TSValue(Items[i]).Profit);
end;
////////////////////////////////////////////////////////////////////////////////
procedure TSCriteria.SetBehavior(ABehavior: word);
begin
  // ���� �������� ������������ �� ��� ������������� � ������� ��������
  case ABehavior of
  2:
    if FBehavior <> 2 then
    begin
      FConvFunc.Free;
      FConvFunc:= nil;
      FValueList:= TSValueList.Create;
    end;
  else
    // ���� �������� � ������������� �� �������������� �� ��-��������� ��������
    // ������� ��������
    if FBehavior = 2 then
    begin
      FValueList.Free;
      FValueList:= nil;
      FConvFunc:= TLinerFN.Create(FMin, FMax);
    end;
  end;
  FBehavior:= ABehavior;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TSCriteria.SetMinValue(AValue: double);
begin
  FMin:= AValue;
  if Assigned(ConvFunc) then ConvFunc.KMin:= AValue;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TSCriteria.SetMaxValue(AValue: double);
begin
  FMax:= AValue;
  if Assigned(ConvFunc) then ConvFunc.KMax:= AValue;
end;
////////////////////////////////////////////////////////////////////////////////
constructor TSCriteria.Create(AOwner: TTreeNode; AName: string;
  AWeight: double = 1; ABehavior: word = 0; AMin: double = 0; AMax: double = 1);
begin
  inherited Create(AOwner, AName, AWeight);
  FDataField:= ''; // �������� �� ������ � �������
  FOldDataField:= ''; // �������� �� ������ � �������  
  FBehavior:= ABehavior;
  FMin:= AMin;
  FMax:= AMax;
  case FBehavior of
  2:begin
      FValueList:= TSValueList.Create;
      FConvFunc:= nil;
    end
  else
    begin
      // ��-��������� �������� ������� ��������
      FConvFunc:= TLinerFN.Create(FMin, FMax);
      FValueList:= nil;
    end;
  end;
  if AOwner is TTreeNode then
  begin
    AOwner.ImageIndex:= 1;
    AOwner.SelectedIndex:= 1;
  end;
end;
////////////////////////////////////////////////////////////////////////////////
destructor TSCriteria.Destroy;
begin
  FConvFunc.Free;
  FConvFunc := nil;
  FValueList.Free;
  FValueList := nil;
  inherited Destroy;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TSCriteria.Assign(Sourse: TCriteria);
begin
  if Sourse is TSCriteria then
  begin
    inherited Assign(Sourse);
    DataField:= TSCriteria(Sourse).DataField;
    OldDataField:= TSCriteria(Sourse).OldDataField;
    Behavior:= TSCriteria(Sourse).Behavior;
    MinValue:= TSCriteria(Sourse).MinValue;
    MaxValue:= TSCriteria(Sourse).MaxValue;
    if Assigned(TSCriteria(Sourse).ConvFunc) then
      TSCriteria(Sourse).ConvFunc.AssignTo(FConvFunc);
    if Assigned(TSCriteria(Sourse).ValueList) then
      TSCriteria(Sourse).ValueList.AssignTo(FValueList);
  end;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TSCriteria.AssignTo(var Distination: TCriteria);
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
  Distination:= TSCriteria.Create(OldOwner, Name, Weight, Behavior, MinValue,
    MaxValue);
  TSCriteria(Distination).DataField:= DataField;
  TSCriteria(Distination).OldDataField:= OldDataField;

  Distination.OnWeightChange:= OldOnWeightChange;
  Distination.Comment.Assign(Comment);

  if Assigned(ConvFunc) then
    ConvFunc.AssignTo(TSCriteria(Distination).FConvFunc)
  else
    TSCriteria(Distination).FConvFunc:= nil;

  if Assigned(ValueList) then
    ValueList.AssignTo(TSCriteria(Distination).FValueList)
  else
    TSCriteria(Distination).FValueList:= nil;

  if Assigned(Distination.OnWeightChange)then
    Distination.OnWeightChange(Distination);
  Distination.OnChange:= OldOnChange;
  if Assigned(Distination.OnChange) then
    Distination.OnChange(Distination);
end;
////////////////////////////////////////////////////////////////////////////////
procedure TSCriteria.ChangeToLiner;
begin
  if Behavior in [0,1] then
  begin
    FConvFunc.Free;
    FConvFunc:= TLinerFN.Create(FMin, FMax);
  end;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TSCriteria.ChangeToBetaDistribution;
begin
  if Behavior in [0,1] then
  begin
    FConvFunc.Free;
    FConvFunc:= TBetaDistributionFN.Create(FMin, FMax);
  end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TSCriteria.ChangeToBeta;
begin
  if Behavior in [0,1] then
  begin
    FConvFunc.Free;
    FConvFunc:= TBetaFN.Create(FMin, FMax);
  end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TSCriteria.ChangeToGaussian;
begin
  if Behavior in [0,1] then
  begin
    FConvFunc.Free;
    FConvFunc:= TGaussianFN.Create(FMin, FMax);
  end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TSCriteria.ChangeToExponential;
begin
  if Behavior in [0,1] then
  begin
    FConvFunc.Free;
    FConvFunc:= TExponentialFN.Create(FMin, FMax);
  end;
end;

////////////////////////////////////////////////////////////////////////////////
function TSCriteria.GetValue: double;
var
  v: Double;
  s: string;
  i: integer;
  DataFound: boolean;
begin
  Result:= 0;
  DataFound:= FALSE;
  if DataField <> '' then
  case FBehavior of
  2:begin // ������������ ��������� ��������
      s:= fMain.frmObject.tblData.FieldByName(DataField).AsString;
      for i:= 0 to ValueList.Count - 1 do
      begin
        if TSValue(ValueList.Items[i]).Value = AnsiUpperCase(s) then
        begin
          Result:= TSValue(ValueList.Items[i]).StdProfit;
          DataFound:= TRUE;
          Break;
        end;
      end;
      if not DataFound then
      begin
        MessageDlg('������ ��� �������� "'+ Name +
          '"'#13' �� ������ � ������ ���������� ��������', mtInformation, [mbOK], 0);
        Abort;
      end;
    end;
  else
    try
      v:= fMain.frmObject.tblData.FieldByName(DataField).AsFloat;
      case fBehavior of
        0: Result:= FConvFunc.Calculate(v);
        1: Result:= 1 - FConvFunc.Calculate(v);
      end;
    except
      MessageDlg('������ ��� �������� "'+ Name +
        '"'#13' �� ������������� ������������', mtInformation, [mbOK], 0);
      Abort;
    end
  end
  else
    begin
      MessageDlg('��� ������ ��� �������� "'+ Name +'"', mtInformation,
        [mbOK], 0);
      Abort;
    end;
end;

////////////////////////////////////////////////////////////////////////////////
function TSCriteria.GetAbsValue: double;
begin
  Result:= 0;
  if DataField <> '' then
    case FBehavior of
      2: Result := GetValue; // ������������ ��������� ��������
      else Result:= fMain.frmObject.tblData.FieldByName(DataField).AsFloat;
    end
  else
    begin
      MessageDlg('��� ������ ��� �������� "'+ Name +'"', mtInformation,
        [mbOK], 0);
      Abort;
    end;
end;

////////////////////////////////////////////////////////////////////////////////
end.