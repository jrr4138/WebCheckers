<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="3">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

      <div>
        <h2>Players Online</h2>
        <ul>
          <#if currentUser??>
            <table width="100%" align="center">
              <tr>
                <th>Players In Lobby</th>
                <th>Players In Game</th>
                <#if !isDemo??>
                <th>Examples</th>
                </#if>
              </tr>
              <tr>

                <td align="center">
                  <p>
                    <#list playersOnline as player>
                        <#if player == currentUser>
                            ${player.name} (${player.formatPlayerAverage()})
                            <#list player.achievements as achievementID>
                                <#if achievementID == 0>
                                    <img src="https://i.ibb.co/GVnpXkv/Green-Badge.jpg" style="width:15px;height:15px">
                                </#if>
                                <#if achievementID == 1>
                                    <img src="https://i.ibb.co/9TrJwnR/Badge-Red.png" style="width:15px;height:15px">
                                </#if>
                                <#if achievementID == 2>
                                    <img src="data:image/png;base64,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" height="15px" width="15px">
                                </#if>
                            </#list>
                            - You <br>
                        <#elseif player.isInLobby()>
                            ${player.name} (${player.formatPlayerAverage()})
                            <#list player.achievements as achievementID>
                                <#if achievementID == 0>
                                    <img src="https://i.ibb.co/GVnpXkv/Green-Badge.jpg" style="width:15px;height:15px">
                                </#if>
                                <#if achievementID == 1>
                                    <img src="https://i.ibb.co/9TrJwnR/Badge-Red.png" style="width:15px;height:15px">
                                </#if>
                                <#if achievementID == 2>
                                    <img src="data:image/png;base64,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" height="15px" width="15px">
                                </#if>
                                <#if achievementID == -1>
                                    <img src="https://www.1999.co.jp/itbig33/10339073.jpg" style="width:15px;height:15px">
                                </#if>
                                <#if achievementID == -2>
                                    <img src="https://live.staticflickr.com/4119/4783836995_9049874089_n.jpg" style="width:15px;height:15px">
                                </#if>
                            </#list>
                            - <a href="/game?opponentName=${player.name}">Challenge</a> <br>
                        </#if>
                    </#list>
                  </p>
                </td>

                <td align="center">
                  <p>
                  <#if gameList??>
                    <#list gameList?keys as k >
                      <#if gameList[k] == "Sputnik">
                        <a href="/spectate?name=${k}">${k}</a> VS <a>${gameList[k]}</a>
                      <#elseif gameList[k] == "WallE">
                        <a href="/spectate?name=${k}">${k}</a> VS <a>${gameList[k]}</a>
                      <#elseif gameList[k] == "Demo">
                          <a href="/spectate?name=${k}">${k}</a> VS <a>${gameList[k]}</a>
                      <#else>
                        <a href="/spectate?name=${k}">${k}</a> VS <a href="/spectate?name=${gameList[k]}">${gameList[k]}</a>
                      </#if>


                      <br>
                    </#list>
                  </#if>
                  </p>
                </td>

                <#if !isDemo??>
                  <td align="center">
                    <p>
                      Jump - <a href="/game?opponentName=jump">Challenge</a> <br>
                      Double Jump - <a href="/game?opponentName=double">Challenge</a> <br>
                      Triple Jump - <a href="/game?opponentName=triple">Challenge</a> <br>
                      King Me - <a href="/game?opponentName=king">Challenge</a> <br>
                      Out-Of-Moves - <a href="/game?opponentName=oom">Challenge</a> <br>
                    </p>
                  </td>
                </#if>

              </tr>
            </table>

            <br>
            <br>
            <div class="key">
              <h3> Badge Key</h3>
                Easy AI - <img src="https://www.1999.co.jp/itbig33/10339073.jpg" style="width:15px;height:15px"> <br>
                Hard AI - <img src="https://live.staticflickr.com/4119/4783836995_9049874089_n.jpg" style="width:15px;height:15px"> <br>
                Defeated Easy AI - <img src="https://i.ibb.co/GVnpXkv/Green-Badge.jpg" style="width:15px;height:15px"> <br>
                Defeated Hard AI - <img src="https://i.ibb.co/9TrJwnR/Badge-Red.png" style="width:15px;height:15px"> <br>
                On a win streak - <img src="data:image/png;base64,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" height="15px" width="15px"> <br>
            </div>
          <#else>
            <p>There are ${playersOnline?size} other players available to play at this time</p>
          </#if>
        </ul>
      </div>
  </div>
</div>
</body>

</html>
