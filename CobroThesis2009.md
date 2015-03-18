# Machine Learning For Object Estimation Using Hierarchical Criteria System

# Machine Learning For Object Estimation Using Hierarchical Criteria System #
_Andrey Styskin, Cybernetics Department of MEPhI(SU), astyskin@gmail.com_

# ABSTRACT #
This paper presents machine learning method to determine parameters of hierarchical criteria system for object estimation based on learning set. The  hierarchical criteria system for object estimation is clear for interpret. Machine learning algorithm is iterative. On each iteration we go through criteria using breadth first algorithm and optimize each criteria while parameters of others criteria are fixed. This algorithm gives good approximation and works for small learning set problem.

  * self checked
  * For each criteria we do just several steps of optimization so we have uniform optimization.
  * sum of squares
  * (better than linear regression)
  * small learning set problem - we can easily check the resulting system

# 1  INTRODUCTION #
In management, science and commercial field we are usually faced with multi-criteria optimization or choosing best object problems. Multi-criteria decision-analysis (MCDA) are used in such problems. But when there are many criteria the standard MCDA-methods becomes hard to use and interpret results and in this case hierarchical methods are used. In these methods criteria are structured in tree/network form and aggregation functions are used to aggregate single criterion to more general ones.
When the problem of building hierarchical models becomes regular it is good idea to machine learn the parameters of model: weights and parameters of aggregation functions. We can use expert values of objects or any other kind of ranking function output estimation for learning set objects.
The resulting optimized model is well interpreted so a researcher could check the validness himself by comparing the parameter estimations with own fillings about the knowledge domain. And this approach works good for small learning set.

# 2 HIERARCHICAL MODEL #
## Definition ##
While building hierarchical model it is required to group and structure measures in form of tree. Lives of such tree are called single criteria. The rest vertexes are called complex criteria. And the root is called integral criterion.
Single and complex criteria differ in units in principle. Single criteria are measured in dimension units and complex are measured in dimensionless units on interval `[0; 1]`.
The values close to 0 indicate the small utility and in opposite values close to 1 indicate big utility.
Hierarchical model present both the set of factors for knowledge domain (in form of single and complex criteria) and logical links between them. On each level there are criteria of the same generality level.  Lets look at an example:

To get estimations of objects with hierarchical model it is also required to define functional links between criteria (where logical links are).
So the hierarchical model is:
  * Knowledge domain organized in tree view;
  * Conversion functions for dimension units that map measures to `[0; 1]` scale;
  * Aggregation functions that aggregate several logical linked measures to more general measure

## Aggregation function ##
**???**
# 3 MACHINE LEARNING ALGORITHM #
# 4 COMPARISON WITH REGRESSION AND OTHERS #
# 5 CONCLUSION #
# References #


В задачах, связанных с научной, управленческой и предпринимательской деятельностью нам постоянно приходится сталкиваться с множеством критериев, по которым необходимо оптимизировать параметры систем, либо оценивать управленческие решения. Для решения таких задач можно использовать многокритериальные методы.
В данном дипломном проекте рассматривается иерархическая система комплексных показателей [1](1.md). Применять традиционные методы даже для 5-6 критериев уже затруднительно, поэтому необходимо их структурировать в виде дерева критериев, а затем проводить агрегирование критериев по дереву.
В случае, когда есть представление о связях между критериями и набор значений входов и соответствующих им значений изучаемого показателя, можно найти такие параметры иерархической модели комплексных показателей, которые будут доставлять изучаемому показателю необходимые значения.
Параметры модели представляют собой веса и коэффициенты жесткости, которые дают исследователю представление о влиянии конкретного фактора на изучаемый показатель и характер совместного влияния этих факторов.
Поскольку модель хорошо интерпретируема и управляема, исследователь может изменять модель, варьировать её параметры и изучать их влияние на показатели. Такой подход позволяет получать представление об объективных оценках собственного и совместного влияния показателей и адекватно выставлять субъективные значения важности факторов в случае задач оптимизации параметров или принятия управленческих решений.
Необходимость в получении объективных значений параметров модели часто возникает в задачах квалиметрии. Такие задачи возникают в компании Яндекс, когда руководителям проектов необходимо сформулировать интегральный показатель качества какого-либо сервиса. Подход, излагаемый далее в дипломном проекте, позволяет понять влияние факторов и измерений на значение интегрального показателя, а также изменить модель согласно собственным предпочтениям менеджера. Именно такой механизм формирования оценки качества с обратной связью позволяет учитывать предпочтения менеджеров и не отрываться от реальной ситуации.
ПОСТАНОВКА ЗАДАЧИ
Целью данного дипломного проекта является создание программного решения для работы с иерархической системой критериев и определения её параметров по обучающей выборке.
Исследователь должен получить инструмент, в котором он сможет строить модель предметной области и получать оценку связанного показателя от простых показателей. У него должна быть возможность задавать экспертные оценки параметрам модели, а также указать источник данных с обучающей выборкой и получать параметры модели, которые наиболее хорошо описывают поведение модели на обучающей выборке. Критерием, по которому определяется степень соответствия модели действительности, выбрано среднеквадратичное отклонение оценок зависимого показателя от его реальных значений.