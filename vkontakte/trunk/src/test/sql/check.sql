select name, start_session, end_session from vkontakte_session a, vkontakte_status b 
where
a.id = b.id
order by start_session


select name, sum(TIMESTAMPDIFF(SECOND, start_session, end_session)) as s, 100*sum(TIMESTAMPDIFF(SECOND, start_session, end_session))/TIMESTAMPDIFF(SECOND, min(start_session), max(end_session))
from vkontakte_session a, vkontakte_status b 
where
a.id = b.id
group by a.id
order by s desc