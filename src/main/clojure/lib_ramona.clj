(ns lib-ramona
  (:use clojure.http.client clojure.contrib.fcase)
  (:import (java.util.regex Pattern)))

;; Trick the server into thinking that the request comes from a browser
(def *user-agent* "Mozilla/5.0")

(def *ramona-url* "http://www.kurzweilai.net/ramona/bottom.php")

(defstruct ramona-response :speech :uid :emotion)

(defn- process-line
  [line response]
  (letfn [(match-helper
           [re response-key]
           (fn [line response]
             (let [matches (re-find re line)]
               (if (nil? matches) response (assoc response response-key (last matches))))))
          (process-line-helper
           [match-fns line response]
           (if (empty? match-fns)
             response
             (recur (rest match-fns) line ((first match-fns) line response))))]
    (process-line-helper
        [(match-helper #"<SPEECH>(.+)</SPEECH>" :speech)
         (match-helper #"<UID>(.+)</UID>" :uid)
         (match-helper #"<EMOTION>(.*)</EMOTION>" :emotion)]
      line
      response)))


(defn- response-complete?
  [response]
  (not (or (nil? (:speech response))
      (nil? (:uid response))
      (nil? (:emotion response)))))

(defn- parse-body-seq
  "Parse the body-seq returned by the server
  and return a struct-map of type ramona-response"
  [body-seq]
  (letfn [(parse-helper
           [response lines]
           (if (or (empty? lines) (response-complete? response))
             response
             (recur (process-line (first lines) response) (rest lines))))]
    (parse-helper (struct-map ramona-response) body-seq)))

(defn start-conversation
  []
  (let [response
        (request *ramona-url* "GET" {"User-Agent" *user-agent*})]
        nil))






