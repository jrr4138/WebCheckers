 <div class="navigation">
  <#if currentUser??>
    <a href="/">my home</a> |
    <#if userInGame?? && userInGame>
      <a href="/game">my game</a> |
    </#if>
    <form id="signout" action="/signout" method="post">
      <a href="#" onclick="event.preventDefault(); signout.submit();">sign out [${currentUser.name}]</a> |
    </form>
      <a href="/rules"> rules </a>
  <#else>
    <a href="/signin">sign in</a>
  </#if>
 </div>
