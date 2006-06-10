unit ConvFunc;

interface
type

  TFunc = class
  private
    FName: string;
    FMin: double;
    FMax: double;
  protected
    procedure SetMin(AValue: double); virtual;
    procedure SetMax(AValue: double); virtual;
  public
    class function ID: word; virtual; abstract;
    procedure AssignTo(var Distination: TFunc); virtual; abstract;
    function Calculate(AValue: Double): Double; virtual; abstract;
    property Name: string read FName write FName ;
    property KMin: double read FMin write SetMin;
    property KMax: double read FMax write SetMax;
  end;

  //////////////////////////////////////////////////////////////////////////////
  // линейная функция
  TLinerFN = class (TFunc)
  private
    FLSatiation: double;
    FRSatiation: double;
    procedure SetLSatiation(const Value: double);
    procedure SetRSatiation(const Value: double);
  public
    class function ID: word; override;
    constructor Create(AMin, AMax: double; ALSatiation: double = 0;
      ARSatiation: double = 1);
    procedure AssignTo(var Distination: TFunc); override;
    function Calculate(AValue: Double): Double; override;
    property LSatiation: double read FLSatiation write SetLSatiation;
    property RSatiation: double read FRSatiation write SetRSatiation;
  end;

  //////////////////////////////////////////////////////////////////////////////
  // функция плотности бета-распределения
  TBetaDistributionFN = class (TFunc)
  private
    FP: double;
    FQ: double;
    FD: double;
    FKm: double;
    FLSatiation: double;
    FRSatiation: double;
    procedure CalculateD;
    procedure SetLSatiation(const Value: double);
    procedure SetRSatiation(const Value: double);
  protected
    procedure SetP(AValue: double);
    procedure SetQ(AValue: double);
    procedure SetMin(AValue: double); override;
    procedure SetMax(AValue: double); override;
  public
    class function ID: word; override;
    constructor Create(AMin, AMax: double; AP: double = 1; AQ: double = 1;
      ALSatiation: double = 0; ARSatiation: double = 0);
    procedure AssignTo(var Distination: TFunc); override;
    function Calculate(AValue: Double): Double; override;
    property P: double read FP write SetP;
    property Q: double read FQ write SetQ;
    property LSatiation: double read FLSatiation write SetLSatiation;
    property RSatiation: double read FRSatiation write SetRSatiation;
    property Km: double read FKm;
  end;

  //////////////////////////////////////////////////////////////////////////////
  // Гауссовская функция
  TGaussianFN = class (TFunc)
  private
    FLSatiation: double;
    FRSatiation: double;
    procedure SetLSatiation(const Value: double);
    procedure SetRSatiation(const Value: double);
  public
    class function ID: word; override;
    constructor Create(AMin, AMax: double; ALSatiation: double = 0;
      ARSatiation: double = 1);
    procedure AssignTo(var Distination: TFunc); override;
    function Calculate(AValue: Double): Double; override;
    property LSatiation: double read FLSatiation write SetLSatiation;
    property RSatiation: double read FRSatiation write SetRSatiation;
  end;

  //////////////////////////////////////////////////////////////////////////////
  // Бета функция
  TBetaFN = class (TFunc)
  private
    FQ: double;
    FP: double;
    FD: double;
    FKm: double;
    FLSatiation: double;
    FRSatiation: double;
    procedure CalculateD;
    procedure SetP(const Value: double);
    procedure SetQ(const Value: double);
    procedure SetLSatiation(const Value: double);
    procedure SetRSatiation(const Value: double);
  protected
    procedure SetMin(AValue: double); override;
    procedure SetMax(AValue: double); override;
  public
    class function ID: word; override;
    constructor Create(AMin, AMax: double; AP: double = 1; AQ: double = 1;
      ALSatiation: double = 0; ARSatiation: double = 1);
    procedure AssignTo(var Distination: TFunc); override;
    function Calculate(AValue: Double): Double; override;
    property P: double read FP write SetP;
    property Q: double read FQ write SetQ;
    property LSatiation: double read FLSatiation write SetLSatiation;
    property RSatiation: double read FRSatiation write SetRSatiation;
    property Km: double read FKm;
  end;

  //////////////////////////////////////////////////////////////////////////////
  // Показательная функция
  TExponentialFN = class (TFunc)
  private
    FBase: double;
    FLSatiation: double;
    FRSatiation: double;
    procedure SetBase(const Value: double);
    procedure SetLSatiation(const Value: double);
    procedure SetRSatiation(const Value: double);
  public
    class function ID: word; override;
    constructor Create(AMin, AMax: double; ABase: Double = 0.01;
      ALSatiation: double = 0; ARSatiation: double = 1);
    procedure AssignTo(var Distination: TFunc); override;
    function Calculate(AValue: Double): Double; override;
    property Base: double read FBase write SetBase;
    property LSatiation: double read FLSatiation write SetLSatiation;
    property RSatiation: double read FRSatiation write SetRSatiation;
  end;

implementation

uses math,Dialogs,Sysutils;
////////////////////////////////////////////////////////////////////////////////
// Функция интерполирующая функцию вероятпости Гауссовского распределения
function Gaussian(X: double; E: double): Double;
var
  t: Double;
  sum, s,f: Double;
  x2: Double;
  m: Integer;
begin
  t:= X;
  sum:= X;
  x2:= X*X;
  m:= 2;
  s:= 0;
  while Abs(s - sum) > E/10 do
  begin
    t:= -x2*t/m;
    s:= sum;
    sum:= s + t/(m+1);
    m:= m + 2;
  end;
  f:= 0.5 + sum/Sqrt(2*Pi);
  Result:= Round(f/E)*E;
end;

////////////////////////////////////////////////////////////////////////////////
// Реализация методов класса TFunc
procedure TFunc.SetMin(AValue: double);
begin
  FMin:= AValue;
end;

procedure TFunc.SetMax(AValue: double);
begin
  FMax:= AValue;
end;

////////////////////////////////////////////////////////////////////////////////
// Реализация методов класса TLinerFN
class function TLinerFN.ID: word;
begin
  Result:= 0;
end;

constructor TLinerFN.Create(AMin, AMax, ALSatiation, ARSatiation: double);
begin
  Name:= 'линейная функция';
  KMin:= AMin;
  KMax:= AMax;
  FLSatiation:= ALSatiation;
  FRSatiation:= ARSatiation;
end;

procedure TLinerFN.SetLSatiation(const Value: double);
begin
  if (Value >= 0) and (Value <= 1) then FLSatiation := Value;
end;

procedure TLinerFN.SetRSatiation(const Value: double);
begin
  if (Value >= 0) and (Value <= 1) then FRSatiation := Value;
end;

procedure TLinerFN.AssignTo(var Distination: TFunc);
begin
  Distination.Free;
  Distination:= TLinerFN.Create(KMin, KMax, LSatiation, RSatiation);
end;

function TLinerFN.Calculate(AValue: Double): Double;
begin
  if (KMin <= AValue) and (AValue <= KMax) then
    Result:= LSatiation + (RSatiation- LSatiation)*(AValue - KMin)/(KMax - KMin)
  else if AValue > KMax then
         Result:= RSatiation
       else Result:= LSatiation;
end;

////////////////////////////////////////////////////////////////////////////////
// Реализация методов класса TBetaDistributionFN
class function TBetaDistributionFN.ID: word;
begin
  Result:= 1;
end;

constructor TBetaDistributionFN.Create(AMin, AMax: double; AP: double;
  AQ: double; ALSatiation: double; ARSatiation: double);
begin
  FName:= 'функция плотности бета-распределения';
  FMin:= AMin;
  FMax:= AMax;
  FP:= AP;
  FQ:= AQ;
  FLSatiation:= ALSatiation;
  FRSatiation:= ARSatiation;
  CalculateD;
end;

procedure TBetaDistributionFN.AssignTo(var Distination: TFunc);
begin
  Distination.Free;
  Distination:=
    TBetaDistributionFN.Create(KMin, KMax, P, Q, LSatiation, RSatiation);
end;

procedure TBetaDistributionFN.SetMin(AValue: double);
begin
  inherited SetMin(AValue);
  CalculateD;
end;

procedure TBetaDistributionFN.SetMax(AValue: double);
begin
  inherited SetMax(AValue);
  CalculateD;
end;

procedure TBetaDistributionFN.CalculateD;
begin
  FKm:= 0;
  if KMin = KMax then
  begin
    FD:= 0;
    FKm:= KMin;
  end
  else if (FP = 0) and (FQ = 0) then FD:= 1
       else
       begin
        FKm:= (KMax*FP+KMin*FQ)/(FP+FQ);
        FD:= 1/(Power(FKm-KMin,FP)*Power(KMax-FKm,FQ));
       end;
end;

procedure TBetaDistributionFN.SetP(AValue: double);
begin
  if (AValue >= 0) and (AValue <= 100) then
  begin
    FP:= AValue;
    CalculateD;
  end;
end;

procedure TBetaDistributionFN.SetQ(AValue: double);
begin
  if (AValue >= 0) and (AValue <= 100) then
  begin
    FQ:= AValue;
    CalculateD;
  end;
end;

procedure TBetaDistributionFN.SetLSatiation(const Value: double);
begin
  if (Value >= 0) and (Value <= 1) then FLSatiation := Value;
end;

procedure TBetaDistributionFN.SetRSatiation(const Value: double);
begin
  if (Value >= 0) and (Value <= 1) then FRSatiation := Value;
end;

function TBetaDistributionFN.Calculate(AValue: Double): Double;
begin
  if (KMin <= AValue) and (AValue <= KMax) then
  begin
    if (AValue <= Km) then
      Result:= LSatiation +
        (1 - LSatiation)*FD*Power(AValue-KMin, FP)*Power(KMax-AValue, FQ)
    else
      Result:=RSatiation +
        (1 - RSatiation)*FD*Power(AValue-KMin, FP)*Power(KMax-AValue, FQ);
  end
  else if AValue > KMax then
         Result:= RSatiation
       else Result:= LSatiation;
end;

////////////////////////////////////////////////////////////////////////////////
// Реализация методов класса TBetaFN
class function TBetaFN.ID: word;
begin
  Result:= 3;
end;

constructor TBetaFN.Create(AMin, AMax, AP, AQ, ALSatiation, ARSatiation: double);
begin
  FName:= 'бета функция';
  FMin:= AMin;
  FMax:= AMax;
  FP:= AP;
  FQ:= AQ;
  FLSatiation:= ALSatiation;
  FRSatiation:= ARSatiation;
  CalculateD;
end;

procedure TBetaFN.SetMin(AValue: double);
begin
  inherited SetMin(AValue);
  CalculateD;
end;

procedure TBetaFN.SetMax(AValue: double);
begin
  inherited SetMax(AValue);
  CalculateD;
end;

procedure TBetaFN.AssignTo(var Distination: TFunc);
begin
  Distination.Free;
  Distination:= TBetaFN.Create(KMin, KMax, P, Q, LSatiation, RSatiation);
end;

procedure TBetaFN.CalculateD;
const
  cnt: Integer = 1000;
var
  hX: Double;
  lF,hF: Double;
  Step: Double;
  D: Double;
begin
  FKm:= 0;
  if KMin = KMax then
  begin
    FD:= 0;
    FKm:= KMin;
  end
  else if (FP = 0) and (FQ = 0) then FD:= 1/(KMax-KMin)
       else
       begin
        FKm:= (KMax*FP+KMin*FQ)/(FP+FQ);
        D:= 0;
        Step:= (KMax - KMin)/cnt;
        hX:= KMin + Step;
        lF:= 0;
        while hX <= KMax do
        begin
          hF:= Power(hX-KMin, FP)*Power(KMax-hX, FQ);
          D:= D + (lF + hF)/2*Step;
          lF:= hF;
          hX:= hX + Step;
        end;
        FD:= 1/D;
       end;
end;

function TBetaFN.Calculate(AValue: Double): Double;
const
  cnt: Integer = 1000; // от этого значения зависит точность вычисления бета-функции
var
  hX: Double;
  lF,hF: Double;
  Step: Double;
begin
  Result:= 0;
    if (KMin <= AValue) and (AValue <= KMax) then
    begin
      Step:= (KMax - KMin)/cnt;
      hX:= KMin + Step;
      lF:= 0;
      while hX <= AValue do
      begin
        hF:= Power(hX-KMin, FP)*Power(KMax-hX, FQ);
        Result:= Result + (lF + hF)/2*Step;
        lF:= hF;
        hX:= hX + Step;
      end;
      Result:= LSatiation + (RSatiation - LSatiation)*Result*FD;
    end
    else if AValue > KMax then
           Result:= RSatiation
         else Result:= LSatiation;
end;

procedure TBetaFN.SetP(const Value: double);
begin
  if (Value >= 0) and (Value <= 100) then
  begin
    FP:= Value;
    CalculateD;
  end;
end;

procedure TBetaFN.SetQ(const Value: double);
begin
  if (Value >= 0) and (Value <= 100) then
  begin
    FQ:= Value;
    CalculateD;
  end;
end;

procedure TBetaFN.SetLSatiation(const Value: double);
begin
  if (Value >= 0) and (Value <= 1) then FLSatiation := Value;
end;

procedure TBetaFN.SetRSatiation(const Value: double);
begin
  if (Value >= 0) and (Value <= 1) then FRSatiation := Value;
end;

////////////////////////////////////////////////////////////////////////////////
// Реализация методов класса TGaussianFN
class function TGaussianFN.ID: word;
begin
  Result:= 2;
end;

constructor TGaussianFN.Create(AMin, AMax, ALSatiation, ARSatiation: double);
begin
  Name:= 'Гауссовская функция';
  KMin:= AMin;
  KMax:= AMax;
  FLSatiation:= ALSatiation;
  FRSatiation:= ARSatiation;
end;

procedure TGaussianFN.SetLSatiation(const Value: double);
begin
  if (Value >= 0) and (Value <= 1) then FLSatiation := Value;
end;

procedure TGaussianFN.SetRSatiation(const Value: double);
begin
  if (Value >= 0) and (Value <= 1) then FRSatiation := Value;
end;

procedure TGaussianFN.AssignTo(var Distination: TFunc);
begin
  Distination.Free;
  Distination:= TGaussianFN.Create(KMin, KMax, LSatiation, RSatiation);
end;

function TGaussianFN.Calculate(AValue: Double): Double;
begin
  if (KMin <= AValue) and (AValue <= KMax) then
    Result:= LSatiation +
      (RSatiation- LSatiation)*(Gaussian((AValue - KMin)/(KMax - KMin)*3.92 - 1.96, 0.001)- 0.025)/0.95
  else if AValue > KMax then
         Result:= RSatiation
       else Result:= LSatiation;
end;

////////////////////////////////////////////////////////////////////////////////
// Реализация методов класса TExponentialFN
class function TExponentialFN.ID: word;
begin
  Result:= 4;
end;

constructor TExponentialFN.Create(AMin, AMax: double; ABase, ALSatiation,
  ARSatiation: Double);
begin
  Name:= 'показательная функция';
  KMin:= AMin;
  KMax:= AMax;
  Base:= ABase;
  FLSatiation:= ALSatiation;
  FRSatiation:= ARSatiation;
end;

procedure TExponentialFN.SetLSatiation(const Value: double);
begin
  if (Value >= 0) and (Value <= 1) then FLSatiation := Value;
end;

procedure TExponentialFN.SetRSatiation(const Value: double);
begin
  if (Value >= 0) and (Value <= 1) then FRSatiation := Value;
end;

procedure TExponentialFN.AssignTo(var Distination: TFunc);
begin
  Distination.Free;
  Distination:= TExponentialFN.Create(KMin, KMax, Base, LSatiation, RSatiation);
end;

function TExponentialFN.Calculate(AValue: Double): Double;
begin
  if (KMin <= AValue) and (AValue <= KMax) then
    Result:= LSatiation +
      (RSatiation- LSatiation)*(1-Power(Base, (AValue - KMin)/(KMax - KMin)))/(1 - Base)
  else if AValue > KMax then
         Result:= RSatiation
       else Result:= LSatiation;
end;

procedure TExponentialFN.SetBase(const Value: double);
begin
  if (Value > 0) and (Value <= 100) and (Value <> 1) then
    FBase := Value
  else
    ShowMessage('Недопустимое значение основания показательной'#13'функции перевода.'#13
      +'Основание должно принадлежать (0, 1) или (1, 100]');
end;

end.
