(ns app.bp-test.logic)


(defn test-get
  [db-comp openai-comp]
  (if (and db-comp openai-comp)
    {:db-comp (str "key db-comp sebagai berikut: " (keys db-comp))
     :openai-comp (str "key openai-comp sebagai berikut: " (keys openai-comp))}
    {:db-comp "Kosong cuy"
     :openai-comp "Kosong cuy"}))