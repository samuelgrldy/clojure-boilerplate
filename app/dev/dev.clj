(ns dev
  (:require
    [com.stuartsierra.component :as component]
    [app.system :as system]
    [clojure.tools.namespace.repl :refer [refresh]]
    [app.utils :as u]
    [cheshire.core :as json]
    [clj-http.client :as http]
    [immutant.util :as log]))

(defonce dev-system (atom nil))

;; create a function that reads config.edn and then get the port
;; looks like only for logging
(defn url
  "Get the port from the config.edn file"
  [path]
  (let [port (-> (u/read-config)
                 (get-in [:server :port]))]
    (str "http://localhost:" port "/api" path)))

;;===== SYSTEM RELATED FUNCTIONS ======

;; Create a function to restart the system

(defn start
  "Starting the webapp"
  []
  (->> (system/create-system)
       (component/start-system)
       (reset! dev-system))
  :system-started)

(defn stop []
  (swap! dev-system component/stop-system))

(defn restart
  []
  (do (stop)
      (print "Restarting the system ... ")
      ;; (Thread/sleep 100)
      ;; (println "plus/minus 5 minutes.")
      (refresh)
      ;; (u/info "Abis refreshing nih hehe")
      ;; (Thread/sleep 100)
      (log/set-log-level! :ALL)
      (start)))

(comment
  ;; if db is not seeded with user, it needs to do this
  (app.plumbing.db/init-admin (:dbase @dev-system) "[insert password]")
  (app.plumbing.db/init-trash (:dbase @dev-system))
  ;; note, [mbe todo], make an interface to run this later on

  ;; mbe basic test note may 10th 2023
  ;;  - [minor - todo - any] admin change role, no notif if the save action succeed
  ;;   - /dashboard/UserManagement
  ;;
  ;;  - [major - todo - any] connecting to content no content-id validation
  ;;   - /dashboard/SkillManagement
  ;;
  ;;  - [major - todo - any] no content id preview
  ;;   -  /dashboard/ContentManagement
  ;;
  )
