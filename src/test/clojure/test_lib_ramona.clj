(ns test-lib-ramona
  (:use clojure.test lib-ramona))

(def response-complete? @#'lib-ramona/response-complete?)
(def process-line @#'lib-ramona/process-line)

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

(run-tests)





