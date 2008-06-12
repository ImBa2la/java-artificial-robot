<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>Репортаж с матча {$game->name} | НЕИнтернет - служба позитивных новостей</title> 
	<meta name="KeyWords" content="Народные НЕ Новости | НЕИнтернет - служба позитивных новостей">
	<meta name="Description" content="Народные НЕ Новости | НЕИнтернет - служба позитивных новостей">
	<link rel="stylesheet" href="../../style.css" type="text/css">
{if $admin != 'admin' && $game->archive eq 0} 
<META HTTP-EQUIV="refresh" CONTENT="60">
{/if} 	 	
	<meta content="text/html; charset=windows-1251" http-equiv=Content-Type>
	<meta http-equiv="Content-Type" content="text/html">
</head>
<body>
	<table width="100%" cellspacing="0" cellpadding="0" id="main_tb">
        	<td id="main_td"><div id="main_menu">
              <div id="page_title">{$game->name|escape}, {$game->score}</div>
        	  <div id="main_menu_bg">
                <div id="main_menu_bg_l">
                  <div id="main_menu_bg_r">
                    <div id="main_menu_shadow">
                      <table cellspacing="0" cellpadding="0" width="100%" id="main_menu_tb">
                        <tr>
                          <td><a href="/">Самая главная</a></td>
                          <td><a href="/news/narodnye/">Народные НЕ Новости</a></td>
                          <td><a href="/news/politika/" >НЕ Политика</a></td>
                          <td><a href="/news/showbiz/">НЕ Шоу Бизнес</a></td>
                          <td><a href="/news/sport/">НЕ Спорт</a></td>
                        </tr>
                      </table>
                    </div>
                  </div>
                </div>
      	    </div>
      	  </div>
       	    <div id="unit_anounces1">
                	<table width="100%" cellspacing="0" cellpadding="0">
                    	<tr>
						
						{if $admin eq 'admin'}
<form action="{$smarty.server.SCRIPT_NAME}?gameId={$smarty.get.gameId}" method="post">
<input type="hidden" name="action" value="game" />
<table border="1">
    <tr>
        <td>Name:</td>
        <td><input type="text" name="nameValue" value="{$game->name}" size="40"></td>
    </tr>
    <tr>
        <td valign="top">Score:</td>
        <td><textarea name="score" cols="40" rows="10">{$game->score}</textarea></td>
    </tr>     
    </tr>     
    <tr>
        <td>Period start time:</td>
        <td><input type="text" name="start_time" value="{$game->start|date_format:"%Y-%m-%d %H:%M:%S"}" size="40"></td>
    </tr>
    <tr>
        <td>Period:</td>
        <td><input type="text" name="start_time" value="{$game->period}" size="40"></td>
    </tr>
    <tr>
        <td colspan="2" align="center"><input type="submit" value="Submit"></td>
    </tr>
</table>
</form>
{else}
{/if}
						
                          <td id="unit_anounces_reportazh">
                           	  <p class="unit_anounces_name">Если вы хотите разместить материалы в своем блоге, пожалуйста размещайте ссылку на <a href="http://www.neinternet.ru/">www.neinternet.ru</a></p>
                           	  
<form action="{$smarty.server.SCRIPT_NAME}?gameId={$smarty.get.gameId}" method="post">
<input type="hidden" name="action" value="comment" />
<table border="1">
    {if $error ne ""}
        <tr>
            <td bgcolor="yellow" colspan="2">
                {if $error eq "user_name_empty"} You must supply a name.
                {/if}
                {if $error eq "user_comment_empty"} You must supply a comment.
                {/if}
            </td>
        </tr>
    {/if}
    <tr>
        <td>Name:</td>
        <td><input type="text" name="userName" value="" size="40"></td>        
    </tr>
    <tr>
        <td>Comment:</td>
        <td>
        <textarea name="userComment" wrap="logical" rows="3" cols="40">
        </textarea>
        </td>
    </tr>
    <tr>
        <td colspan="2" align="center"><input type="submit" value="Submit"></td>
    </tr>
</table>
</form>                                       	  
                           	  
							  
							  {if $admin eq 'admin'}
<form action="{$smarty.server.SCRIPT_NAME}?gameId={$smarty.get.gameId}" method="post">
<input type="hidden" name="action" value="data" />
<input type="hidden" name="editId" value="{$editId}" />
{/if}
							  
                            	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="unit_anounces_name">
                            	  {foreach from=$data item="entry"}
        <tr>
                <td class="unit_anounces_reportazh_time">{$entry.mnt}'</td>
            {if $editId eq $entry.id}
                <td><textarea name="val" cols="60" rows="3">{$entry.val|escape}</textarea></td>
            {else}
                <td class="unit_anounces_reportazh_comment">{$entry.val}
                </td>
            {/if}
            {if $admin eq 'admin'}
                <td><a href="{$smarty.server.SCRIPT_NAME}?gameId={$smarty.get.gameId}&action=edit&id={$entry.id}">edit</a></td>
                <td><a href="{$smarty.server.SCRIPT_NAME}?gameId={$smarty.get.gameId}&action=delete&id={$entry.id}">delete</a> </td>
            {/if}
        </tr>
    {/foreach}
                          	  </table>
                            	<div class="unit_anounces_slasher_inside">&nbsp;</div>
								
								{if $admin eq 'admin'}
<input type="submit" value="Submit">
</form>
{/if}

{if $admin eq 'admin'}
<form action="{$smarty.server.SCRIPT_NAME}?gameId={$smarty.get.gameId}" method="post">
<input type="hidden" name="action" value="submit" />
<table border="1">

    {if $error ne ""}
        <tr>
            <td bgcolor="yellow" colspan="2">
                {if $error eq "comment_empty"} You must supply a comment.
                {/if}
            </td>
        </tr>
    {/if}
    <tr>
        <td>Comment:</td>
        <td>
        <textarea width="400px" height="400px" name="Comment" wrap="logical" rows="21" cols="42">
                {$post.Comment|escape}
        </textarea>
        </td>
    </tr>
    <tr>
        <td colspan="2" align="center"><input type="submit" value="Submit"></td>
    </tr>
</table>
</form>
{/if}  

<table width="100%" border="0" cellspacing="0" cellpadding="0" class="unit_anounces_name">
{foreach from=$comments item="entry"}
        <tr>
                <td class="unit_anounces_reportazh_comment">{$entry.name|escape}</td>
                <td class="unit_anounces_reportazh_comment">{$entry.val|escape}</td>
        </tr>
{/foreach}
</table>
    
								                         	  
                           	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="unit_anounces_name">
                            </table>
                       	  </td>
                            <td width="37%">
                        
	                        	<!-- NEWS /-->
    	                        <div id="unit_news1">
        	                    	<table cellspacing="0" cellpadding="0">
            	                    	<tr>
                	                    	<td id="unit_news_arch1" width="100%"><a href="/news/politika/">в архив</a></td>
                    	                	<td id="unit_news_tit1"><img src="../../images/unit_news_tit_politika.gif" alt="" width="166" height="30" title=""></td>
                        	            </tr><tr>
                            	    	</tr>
	                                </table>
    	                            
        	                    	<table width="100%" cellspacing="0" cellpadding="0" class="unit_news_item">
            	                    	<tr>
                	                    	<td><img src="../../images_content/nepolitika/neinternet_politika_hromaet_obrazovanie_middle.jpg" alt="" width="115" height="94" class="unit_news_pic" title=""></td>
                    	                    <td width="100%"><a href="/news/politika/" class="unit_news_name">Хромает Образование? На костыли!!!</a>
                    	                      <div class="unit_news_theme">[из серии новостей про Путина]</div>
                                		        Дочь Владимира Путина, собирается получать второе высшее  образование, конкурс огромный, 60 институтов на место.<a href="/news/politika/" class="unit_news_more">подробнее</a>	                                        </td>
    	                                </tr>
        	                        </table>
	                            	<table width="100%" cellspacing="0" cellpadding="0" class="unit_news_item">
	                                    <tr>
	                                    	<td><img src="../../images_content/nepolitika/neinternet_politika_moldaviya_middle.jpg" alt="" width="115" height="98" class="unit_news_pic" title=""></td>
	                                        <td width="100%">
	                                        	<a href="/news/politika/" class="unit_news_name">В Молдавии президентом становится, тот, кто лучше всех клеит обои.</a>
	                                            <div class="unit_news_theme">[молдавские]</div>
	                                            <div>Недавно в Кишиневе прошли президентские игры  «Весёлые старты», нам удалось пообщаться с организатором этого спортивно-политического  конкурса Василием Тряпкиным...</div>
                                                <a href="/news/politika/" class="unit_news_more">подробнее</a>	                                        </td>
	                                    </tr>
	                                </table>
	                            	<table width="100%" cellspacing="0" cellpadding="0" class="unit_news_item">
	                                    <tr>
	                                    	<td><img src="../../images_content/nepolitika/neinternet_politika_medvedev_middle.jpg" alt="" width="115" height="83" class="unit_news_pic" title=""></td>
                                          <td width="100%">
	                                        	<a href="/news/politika/" class="unit_news_name">Медведев не успевает поздравлять наших с Победой.</a>
	                                            <div class="unit_news_theme">[опять про победы]</div>
                                              <div>
	                                              <p>Президент РФ Медведев то и дело  поздравляет наших спортсменов с победами.</p>
                                              Дмитрий Анатолиевич лично позвонил и поздравил Диму Билана, Евгения  Плющенко, Яну Рудковскую, Эдвина Мартона, всех игроков Зенита, всех участников  сборной России по Баскетболу, Ковальчука и компанию, лично поздравил по  телефону Севу Синезадова с победой на Олимпиаде по ОБЖ...</div>
	                                            <a href="/news/politika/" class="unit_news_more">подробнее</a>	                                        </td>
	                                    </tr>
	                                </table>
	                            </div>                            </td>
                        </tr>
                    </table>
              </div>			</td>
        </tr>    <tr>
              <td id="footer"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="30%"> Copyright © 2008   neinternet.ru<br />
                        <br />
                      Все права защищены - перезащищены.<br />
                      Любое использование материалов допускается<br />
                      только с письменного согласия.</td>
                    <td id="sponsor"> Наш НЕСпонсор сегодня<br />
                        <br />
                      Фитнес клуб «Василий» - это вам не Николай. <br />                    </td>
                    <td width="20%"><table align="center" cellpadding="0" cellspacing="0" id="counters">
                        <tr>
                          <td><a href="#"><img src="/images/counter1.gif" alt="" title="" /></a></td>
                          <td><!--LiveInternet counter--><script type="text/javascript"><!--
document.write("<a href='http://www.liveinternet.ru/click' "+
"target=_blank><img src='http://counter.yadro.ru/hit?t57.6;r"+
escape(document.referrer)+((typeof(screen)=="undefined")?"":
";s"+screen.width+"*"+screen.height+"*"+(screen.colorDepth?
screen.colorDepth:screen.pixelDepth))+";u"+escape(document.URL)+
";"+Math.random()+
"' alt='' title='LiveInternet' "+
"border=0 width=88 height=31><\/a>")//--></script><!--/LiveInternet--></td>
                        </tr>
                    </table></td>
                  </tr>
              </table></td>
            </tr>
    </table>
</body>
</html>