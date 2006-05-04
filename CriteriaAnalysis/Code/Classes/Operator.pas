unit Operator;

interface

uses Criteria;

type
  TOperator = class
  private
    FName: string;
    FOwner: TCriteria;
  public
    class function ID: word; virtual; abstract;
    constructor Create(AOwner: TCriteria);
    function Calculate: double; virtual; abstract;
    procedure AssignTo(var Distination: TOperator); virtual; abstract;
    property Name: string read FName write FName ;
    property Owner: TCriteria read FOwner write FOwner;
  end;

  TAdditiveOpr = class(TOperator)
  public
    class function ID: word; override;
    constructor Create(AOwner: TCriteria);
    function Calculate: double; override;
    procedure AssignTo(var Distination: TOperator); override;
  end;

  TPowerOpr = class(TOperator)
    FPower: double;
    FLambda: double;
  protected
    function GetLambda: double;
    procedure SetLambda(AValue: double);
  public
    constructor Create(AOwner: TCriteria; ALambda:double = 0.5); virtual;
    function CalculateF(AValue: double): double; virtual; abstract;
    function CalculateG(AValue: double): double; virtual; abstract;
    property Lambda: double read GetLambda write SetLambda;
  end;

  TPowerIOpr = class(TPowerOpr)
  public
    class function ID: word; override;
    constructor Create(AOwner: TCriteria; ALambda:double = 0.5); override;
    procedure AssignTo(var Distination: TOperator); override;
    function Calculate: double; override;
    function CalculateF(AValue: double): double; override;
    function CalculateG(AValue: double): double; override;
  end;

  TPowerIIOpr = class(TPowerOpr)
  public
    class function ID: word; override;
    constructor Create(AOwner: TCriteria; ALambda:double = 0.5); override;
    procedure AssignTo(var Distination: TOperator); override;
    function Calculate: double; override;
    function CalculateF(AValue: double): double; override;
    function CalculateG(AValue: double): double; override;
  end;

  TDoublePowerOpr = class(TPowerOpr)
  public
    class function ID: word; override;
    constructor Create(AOwner: TCriteria; ALambda:double = 0.5); override;
    procedure AssignTo(var Distination: TOperator); override;
    function Calculate: double; override;
    function CalculateF(AValue: double): double; override;
    function CalculateG(AValue: double): double; override;
  end;

  TMultiplicativeOpr = class(TOperator)
    FConst: double;
    FLambda: double;
  protected
    function GetLambda: double;
    procedure SetLambda(AValue: double);
  public
    class function ID: word; override;
    constructor Create(AOwner: TCriteria; ALambda:double = 0.5);
    function Calculate: double; override;
    procedure RecalculateC;
    procedure AssignTo(var Distination: TOperator); override;
    property Lambda: double read GetLambda write SetLambda;
  end;

implementation

uses Sysutils, CCriteria, Dialogs, Math, comctrls;

////////////////////////////////////////////////////////////////////////////////
constructor TOperator.Create(AOwner: TCriteria);
begin
  if not (AOwner is TCCriteria) then
    raise Exception.Create('Владельцем оперетора агрегирования может'#13 +
     'быть только комплексный критерий');
  inherited Create;
  FOwner:= AOwner;
end;

////////////////////////////////////////////////////////////////////////////////
class function TAdditiveOpr.ID: word;
begin
  Result:= 0;
end;

////////////////////////////////////////////////////////////////////////////////
constructor TAdditiveOpr.Create(AOwner: TCriteria);
begin
  inherited Create(AOwner);
  Name:= 'аддитивный оператор';
end;

////////////////////////////////////////////////////////////////////////////////
function TAdditiveOpr.Calculate: double;
var
  ChildCriteria: TCriteria;
begin
  Result:= 0;
  ChildCriteria:= TCCriteria(FOwner).GetFistChild;
  if not Assigned(ChildCriteria) then
    ShowMessage('Нет данных для агрегирования критерия "' + FOwner.Name +
      '"'#13'Значение критерия приравнено к нулю');
  while ChildCriteria <> nil do
  begin
    Result:= Result + ChildCriteria.StdWeight * ChildCriteria.Value;
    ChildCriteria:= TCCriteria(FOwner).GetNextChild(ChildCriteria);
  end;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TAdditiveOpr.AssignTo(var Distination: TOperator);
var
  OperatorOwner: TCriteria;
begin
  // Запоминаем критерий, который владеет оператором
  OperatorOwner:= Distination.Owner;
  // Освобождаем память занимаемую оператором
  Distination.Free;
  // Создаем новый оператор
  Distination:= TAdditiveOpr.Create(OperatorOwner);
end;

////////////////////////////////////////////////////////////////////////////////
constructor TPowerOpr.Create(AOwner: TCriteria; ALambda:double = 0.5);
begin
  inherited Create(AOwner);
  Lambda:= ALambda;
  Name:= 'абстрактный степенной оператор';
end;

////////////////////////////////////////////////////////////////////////////////
function TPowerOpr.GetLambda: double;
begin
  Result:= FLambda;
end;

////////////////////////////////////////////////////////////////////////////////
procedure TPowerOpr.SetLambda(AValue: double);
begin
  if (Avalue < 1) and (AValue > 0) then
  begin
     FPower:= Ln(AValue) / Ln(1 - AValue);
     FLambda:= AValue;
  end
  else Abort;
end;

////////////////////////////////////////////////////////////////////////////////
class function TPowerIOpr.ID: word;
begin
  Result:= 1;
end;

////////////////////////////////////////////////////////////////////////////////
constructor TPowerIOpr.Create(AOwner: TCriteria; ALambda:double = 0.5);
begin
  inherited Create(AOwner, ALambda);
  Name:= 'степенной оператор II типа';
end;

////////////////////////////////////////////////////////////////////////////////
procedure TPowerIOpr.AssignTo(var Distination: TOperator);
var
  OperatorOwner: TCriteria;
begin
  // Запоминаем критерий, который владеет оператором
  OperatorOwner:= Distination.Owner;
  // Освобождаем память занимаемую оператором
  Distination.Free;
  // Создаем новый оператор
  Distination:= TPowerIOpr.Create(OperatorOwner, FLambda);
end;

////////////////////////////////////////////////////////////////////////////////
function TPowerIOpr.Calculate: double;
var
  ChildCriteria: TCriteria;
begin
  Result:= 0;
  ChildCriteria:= TCCriteria(FOwner).GetFistChild;
  if not Assigned(ChildCriteria) then
    ShowMessage('Нет данных для агрегирования критерия "' + FOwner.Name +
      '"'#13'Значение критерия приравнено к нулю');
  while ChildCriteria <> nil do
  begin
    Result:= Result + ChildCriteria.StdWeight * Power(ChildCriteria.Value,FPower);
    ChildCriteria:= TCCriteria(FOwner).GetNextChild(ChildCriteria);
  end;
end;

////////////////////////////////////////////////////////////////////////////////
// Функция необходима для построения графика оператора агрегирования
function TPowerIOpr.CalculateF(AValue: double): double;
begin
  Result:= Power(AValue, 1/FPower);
end;

////////////////////////////////////////////////////////////////////////////////
// Функция необходима для построения графика оператора агрегирования
function TPowerIOpr.CalculateG(AValue: double): double;
begin
  Result:= AValue;
end;

////////////////////////////////////////////////////////////////////////////////
class function TPowerIIOpr.ID: word;
begin
  Result:= 2;
end;

////////////////////////////////////////////////////////////////////////////////
constructor TPowerIIOpr.Create(AOwner: TCriteria; ALambda:double = 0.5);
begin
  inherited Create(AOwner);
  Lambda:= ALambda;
  Name:= 'степенной оператор II типа';
end;

////////////////////////////////////////////////////////////////////////////////
function TPowerIIOpr.Calculate: double;
var
  ChildCriteria: TCriteria;
begin
  Result:= 0;
  ChildCriteria:= TCCriteria(FOwner).GetFistChild;
  if not Assigned(ChildCriteria) then
    ShowMessage('Нет данных для агрегирования критерия "' + FOwner.Name +
      '"'#13'Значение критерия приравнено к нулю');
  while ChildCriteria <> nil do
  begin
    Result:= Result + ChildCriteria.StdWeight * ChildCriteria.Value;
    ChildCriteria:= TCCriteria(FOwner).GetNextChild(ChildCriteria);
  end;
  Result:= Power(Result, FPower);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TPowerIIOpr.AssignTo(var Distination: TOperator);
var
  OperatorOwner: TCriteria;
begin
  // Запоминаем критерий, который владеет оператором
  OperatorOwner:= Distination.Owner;
  // Освобождаем память занимаемую оператором
  Distination.Free;
  // Создаем новый оператор
  Distination:= TPowerIIOpr.Create(OperatorOwner, FLambda);
end;

////////////////////////////////////////////////////////////////////////////////
// Функция необходима для построения графика оператора агрегирования
function TPowerIIOpr.CalculateF(AValue: double): double;
begin
  Result:= AValue;
end;

////////////////////////////////////////////////////////////////////////////////
// Функция необходима для построения графика оператора агрегирования
function TPowerIIOpr.CalculateG(AValue: double): double;
begin
  Result:= Power(AValue, FPower);
end;

////////////////////////////////////////////////////////////////////////////////
class function TDoublePowerOpr.ID: word;
begin
  Result:= 3;
end;


////////////////////////////////////////////////////////////////////////////////
constructor TDoublePowerOpr.Create(AOwner: TCriteria; ALambda:double = 0.5);
begin
  inherited Create(AOwner);
  Lambda:= ALambda;
  Name:= 'двойной степенной оператор';
end;

////////////////////////////////////////////////////////////////////////////////
function TDoublePowerOpr.Calculate: double;
var
  ChildCriteria: TCriteria;
begin
  Result:= 0;
  ChildCriteria:= TCCriteria(FOwner).GetFistChild;
  if not Assigned(ChildCriteria) then
    ShowMessage('Нет данных для агрегирования критерия "' + FOwner.Name +
      '"'#13'Значение критерия приравнено к нулю');
  while ChildCriteria <> nil do
  begin
    Result:= Result+ ChildCriteria.StdWeight*Power(ChildCriteria.Value, FPower);

    ChildCriteria:= TCCriteria(FOwner).GetNextChild(ChildCriteria);
  end;
  Result:= Power(Result, FPower);
end;

////////////////////////////////////////////////////////////////////////////////
procedure TDoublePowerOpr.AssignTo(var Distination: TOperator);
var
  OperatorOwner: TCriteria;
begin
  // Запоминаем критерий, который владеет оператором
  OperatorOwner:= Distination.Owner;
  // Освобождаем память занимаемую оператором
  Distination.Free;
  // Создаем новый оператор
  Distination:= TDoublePowerOpr.Create(OperatorOwner, FLambda);
end;

////////////////////////////////////////////////////////////////////////////////
// Функция необходима для построения графика оператора агрегирования
function TDoublePowerOpr.CalculateF(AValue: double): double;
begin
  Result:= Power(AValue, 1/FPower);
end;

////////////////////////////////////////////////////////////////////////////////
// Функция необходима для построения графика оператора агрегирования
function TDoublePowerOpr.CalculateG(AValue: double): double;
begin
  Result:= Power(AValue, FPower);
end;

////////////////////////////////////////////////////////////////////////////////
// TMultiplicativeOpr
class function TMultiplicativeOpr.ID: word;
begin
  Result:= 4;
end;

function TMultiplicativeOpr.GetLambda: double;
begin
  Result:= FLambda;
end;

procedure TMultiplicativeOpr.SetLambda(AValue: double);
begin
  if (AValue > 0) and (AValue < 1) then
  begin
    FLambda:= AValue;
    RecalculateC;
  end
  else Abort;
end;

constructor TMultiplicativeOpr.Create(AOwner: TCriteria; ALambda:double = 0.5);
begin
  inherited Create(AOwner);
  Lambda:= ALambda;
  Name:= 'мультипликативный оператор';
end;

////////////////////////////////////////////////////////////////////////////////
function Calculate(L,C: Double; var v: array of Double): Double;
var i: Integer;
begin
  Result := 1;
  for i := Low(V) to High(V) do
    Result := Result*(1+C*2*L*V[i]);
  Result := Result - C - 1;
end;

function CalculateG(L,C: Double; var v: array of Double): Double;
var i: Integer;
begin
  Result := 1;
  for i := Low(V) to High(V) do
    Result := Result*(1+C*2*L*V[i]);
  Result := (Result - 1)/C;
end;

function CalulateC(L, Precision: Double; var v: array of Double): Double;
var
  g: Double;
  Right, Delta: Double;
  LeftValue, RightValue: Double;
begin
  if Length(v) > 1 then
  begin
    g := 0; Result := 0;
    LeftValue := 0;
    Delta := 10;
    Right := Delta;
    repeat
      RightValue:= Calculate(L, Right, v);
      if ((RightValue > 0) and (LeftValue <= 0))
        or((RightValue < 0) and (LeftValue > 0)) then
      begin
        g := CalculateG(L, Right, v);
        Result := Right;
        Delta:= Delta/2;
        Right := Right - Delta;
      end
      else
      begin
        LeftValue := RightValue;
        Right := Right + Delta;
      end;
  until ((g > 1 - Precision) and (g < 1 + Precision)) or (Delta = 0);
  end
  else Result := 1;
end;
////////////////////////////////////////////////////////////////////////////////
procedure TMultiplicativeOpr.RecalculateC;
var
  ChildCriteria: TCriteria;
  v: array of Double;
  pc: TCriteria;
begin
  FConst := 1;
  if Assigned(FOwner.Owner) then
    pc:= FOwner
  else pc:= FOwner.BaseCriteria;

  ChildCriteria:= TCCriteria(pc).GetFistChild;
  while ChildCriteria <> nil do
  begin
    SetLength(v, Length(v) + 1);
    v[High(v)] := ChildCriteria.StdWeight;
    ChildCriteria:= TCCriteria(pc).GetNextChild(ChildCriteria);
  end;

  if Length(v) > 0 then
    if FLambda <= 0.5 then
      FConst := CalulateC(Min(FLambda, 0.49999), 0.00001, v) // конъюнлтивный оператор
    else
      FConst := CalulateC(1-Max(FLambda, 0.50001), 0.00001, v); // дизюнктивный оператор
  v:= nil; //Освобождение динамического массива
end;

function TMultiplicativeOpr.Calculate: double;
var
  ChildCriteria: TCriteria;
  L: Double;
begin
 Result:= 0;
  ChildCriteria:= TCCriteria(FOwner).GetFistChild;
  if not Assigned(ChildCriteria) then
    ShowMessage('Нет данных для агрегирования критерия "' + FOwner.Name +
      '"'#13'Значение критерия приравнено к нулю')
  else
  begin
    Result:= 1;
    if FLambda <= 0.5 then // конъюнктивный оператор
    begin
      while ChildCriteria <> nil do
      begin
        L:= Min(FLambda, 0.49999);
        Result:=
          Result*(1+FConst*2*L*ChildCriteria.StdWeight*ChildCriteria.Value);
        ChildCriteria:= TCCriteria(FOwner).GetNextChild(ChildCriteria);
      end;
      Result:= (Result - 1)/FConst;
    end
    else
    begin // дизъюнктивный оператор
      while ChildCriteria <> nil do
      begin
        L:= Max(FLambda, 0.50001);
        Result:=
          Result*(1+FConst*2*(1-L)*ChildCriteria.StdWeight*(1-ChildCriteria.Value));
        ChildCriteria:= TCCriteria(FOwner).GetNextChild(ChildCriteria);
      end;
      Result:= 1-(Result-1)/FConst;
    end;
  end;
end;

procedure TMultiplicativeOpr.AssignTo(var Distination: TOperator);
var
  OperatorOwner: TCriteria;
begin
  // Запоминаем критерий, который владеет оператором
  OperatorOwner:= Distination.Owner;
  // Освобождаем память занимаемую оператором
  Distination.Free;
  // Создаем новый оператор
  Distination:= TMultiplicativeOpr.Create(OperatorOwner, FLambda);
end;
////////////////////////////////////////////////////////////////////////////////
end.
