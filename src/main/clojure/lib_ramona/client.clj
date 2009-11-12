(ns lib-ramona.client
  (:use lib-ramona))

(defn- helper
  [dialog uid]
  (let [my-speech (read-line)
        new-dlg (converse dialog uid my-speech)]
    (println (:speech (ffirst new-dlg)))
    (recur new-dlg uid)))

(defn start
  []
  (let [init-response (init-conversation)]
    (println (:speech init-response))
    (helper '() (:uid init-response))))
