unit Options;

interface
var
  VerticalDependenceCriteria: Integer;
  HorizontalDependenceCriteria: Integer;
  VerticalDependenceCriteriaUnit: Integer;
  HorizontalDependenceCriteriaUnit: Integer;

implementation

initialization
  VerticalDependenceCriteria := 0; // первый в списке критерий
  HorizontalDependenceCriteria := 0; // первый в списке критерий
  VerticalDependenceCriteriaUnit := 0; // в относительных величинах
  HorizontalDependenceCriteriaUnit := 0; // в относительных величинах
end.
