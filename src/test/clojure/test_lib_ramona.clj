(ns test-lib-ramona
  (:use clojure.test lib-ramona))

(def response-complete? @#'lib-ramona/response-complete?)
(def process-line @#'lib-ramona/process-line)
(def parse-body-seq @#'lib-ramona/parse-body-seq)

(deftest test-response-complete
  (is (true? (response-complete?
               (struct-map ramona-response :uid "123" :speech "OK" :emotion ""))))
  (is (false? (response-complete?
                (struct-map ramona-response :uid nil :speech "OK" :emotion nil)))))

(deftest test-process-line
  (is (=
        (struct-map ramona-response :uid "123" :speech nil :emotion nil)
        (process-line "<UID>123</UID>" (struct-map ramona-response :uid nil :speech nil :emotion nil))))
  (is (=
        (struct-map ramona-response :uid "123" :speech "OK" :emotion nil)
        (process-line "<SPEECH>OK</SPEECH>" (struct-map ramona-response :uid "123" :speech nil :emotion nil))))
  (is (=
        (struct-map ramona-response :uid "123" :speech "OK" :emotion "")
        (process-line "<EMOTION></EMOTION>" (struct-map ramona-response :uid "123" :speech "OK" :emotion nil))))
  (is (=
        (struct-map ramona-response :uid "123" :speech nil :emotion nil)
        (process-line "<!-- SOMETHING ELSE -->" (struct-map ramona-response :uid "123" :speech nil :emotion nil)))))

(deftest test-parse-body-seq
  (let [body-seq '("<!--" "HTTP/1.1 200 OK" "Date: Thu, 12 Nov 2009 15:49:12 GMT" "Server: Jetty/3.1.5 (Windows 2000 5.0 x86)" "Servlet-Engine: Jetty/3.1 (JSP 1.1; Servlet 2.2; java 1.4.2_08)" "Content-Type: text/html; charset=UTF-8" "Transfer-Encoding: chunked" "" "5b" "<SPEECH>Hi, I'm Ramona. What can I call you?</SPEECH>" "<UID>208329</UID>" "<EMOTION></EMOTION>" "-->" "<html>" "<head>" "<script src=\"/include/global.js\"></script>" "<script language=\"JavaScript\">" "<!--" "" "function speak( text ) {" "" "\tif (parent.ramona.LifeFXPlayer)" "\t\tparent.ramona.LifeFXPlayer.PlayText(text);" "\t" "}" "" "function changeImages() {" "  if (document.images) {" "    for (var i=0; i<changeImages.arguments.length; i+=2) {" "      document[changeImages.arguments[i]].src = changeImages.arguments[i+1];" "    }" "  }" "}" "" "" "speak('Hi, I\\'m Ramona. What can I call you? ');" "//-->" "</script>" "<link rel=\"stylesheet\" type=\"text/css\" href=\"/include/gloss.css\">" "</head>" "<body bgcolor=\"006699\" text=\"FFFFFF\" onLoad=\"document.responseForm.txtUser.focus();\" link=\"yellow\" vlink=\"yellow\" alink=\"yellow\" TOPMARGIN=0>" "<center>" "<a href=\"/meme/frame.html?m=9\" target=\"mainTarget\" onMouseOver=\"changeImages('ramona_story','/images/ramona_story-over.gif'); return true;\" onMouseOut=\"changeImages('ramona_story','/images/ramona_story.gif'); return true;\"><img name=\"ramona_story\" src=\"/images/ramona_story.gif\" width=\"250\" height=\"30\" border=\"0\" alt=\"Read All About Ramona!\"></a>" "<table border=\"0\">" "<tr>" "<td><p>Even a virtual person likes to chat.  Just answer Ramona's questions, and let the conversation flow from there.</p>" "</td>" "</tr>" "<tr><td></td></tr>" "<tr>" "<td>" "<form action=\"bottom.php\" name=\"responseForm\" method=\"GET\">" "<input type=\"text\" name=\"txtUser\">" "<input type=\"submit\" value=\"Respond\">" "<input type=\"hidden\" value=\"1\" name=\"submit\">" "<input type=\"hidden\" name=\"userid\" value=\"208329\">" "</form>" "<p class=\"ramona\">Hi, I'm Ramona. What can I call you?</p>" "<form><p><input type=\"button\" value=\"Start Over\" onClick=\"top.location.reload()\"></p></form>" "<form action=\"transcript.php\" method=\"POST\" target=\"_blank\"><input type=\"hidden\" name=\"userid\" value=\"208329\"><input type=\"submit\" value=\"Get Transcript\"></form>" "</td>" "</tr>" "</table>" "<p><a href=\"/diva\" target=\"ramona\"><b>Chat with Ramona - KurzweilAI.net Hostess</b></a></p>" "</center>" "</body>" "</html>")]
    (is (= (struct-map ramona-response :uid "208329" :speech "Hi, I'm Ramona. What can I call you?" :emotion "") (parse-body-seq body-seq)))))



(run-tests)





