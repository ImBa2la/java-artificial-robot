program McPD;

{%ToDo 'McPD.todo'}

uses
  Forms,
  MainF in 'Form\MainF.pas' {fMain},
  ObjectFrm in 'Frame\Object\ObjectFrm.pas' {frmObject: TFrame},
  CriteriaFrm in 'Frame\Criteria\CriteriaFrm.pas' {frmCriteria: TFrame},
  ProblemDM in 'DM\ProblemDM.pas' {dmProblem: TDataModule},
  Criteria in 'Classes\Criteria.pas',
  ProblemPropertiesF in 'Form\ProblemProperties\ProblemPropertiesF.pas' {fProblemProperties},
  SCriteria in 'Classes\SCriteria.pas',
  SCCnvrsnPrmtrFrm in 'Frame\SCConversionParametr\SCCnvrsnPrmtrFrm.pas' {frmSCCnvrsnPrmtr: TFrame},
  SCValuesFrm in 'Frame\SCValues\SCValuesFrm.pas' {frmSCValues: TFrame},
  ConvFunc in 'Classes\ConvFunc.pas',
  LinerFuncFrm in 'Frame\LinerFunc\LinerFuncFrm.pas' {frmLinerFunc: TFrame},
  BetaDistributionFuncFrm in 'Frame\BetaDistributionFunc\BetaDistributionFuncFrm.pas' {frmBetaDistributionFunc: TFrame},
  CCriteria in 'Classes\CCriteria.pas',
  Operator in 'Classes\Operator.pas',
  SCValueDlg in 'Form\SCValueDlg\SCValueDlg.pas' {dlgSCValue},
  ResultF in 'Form\Result\ResultF.pas' {fResult},
  BetaFuncFrm in 'Frame\BetaFunc\BetaFuncFrm.pas' {frmBetaFunc: TFrame},
  ExponentialFuncFrm in 'Frame\ExponentialFunc\ExponentialFuncFrm.pas' {frmExponentialFunc: TFrame},
  GaussianFuncFrm in 'Frame\GaussianFunc\GaussianFuncFrm.pas' {frmGaussianFunc: TFrame},
  DependenceF in 'Form\Dependence\DependenceF.pas' {fDependence},
  Options in 'Classes\Options.pas';

{$R *.RES}

begin
  Application.Initialize;
  Application.Title := 'McPD';
  Application.CreateForm(TfMain, fMain);
  Application.Run;
end.
