(ns app.plumbing.db
  (:require
    [com.stuartsierra.component :as component]
    [monger.collection :as mc]
    [app.utils :as u]
    [monger.core :as mg]))

(defrecord Dbase [db-mongo-config]
  component/Lifecycle
  (start [this]
    (u/info "Starting the database component with the following key")
    (u/pres
      #_db-mongo-config
      (keys db-mongo-config))
    (let [conn-content (mg/connect (assoc db-mongo-config :uri
                                                          (:uri-content db-mongo-config)))
          db-content (mg/get-db conn-content (:db-content db-mongo-config))


          ;conn-universal (mg/connect (assoc db-mongo-config :uri
          ;                                   (:uri-universal db-mongo-config)))
          ;db-universal (mg/get-db conn-universal (:db-universal db-mongo-config))
          ;;add more db and its conn here

          ;scheduler-running? (atom true)
          ]
      (u/info "Starting the database and the dbref")
      (merge this {:conn-content            conn-content
                   :db-content              db-content
                   ;:scheduler-running?      scheduler-running?
                   })))
  (stop [this]
    (do (mg/disconnect (:conn-content this))
        (mg/disconnect (:conn-universal this)))
    (u/info "Database stopped")
    (dissoc this :conn-content :conn-universal)
    ;(reset! (:scheduler-running? this) false)
    ;(u/info "Scheduler stopped")
    ))

(defn create-database-component [db-mongo-config]
  (map->Dbase {:db-mongo-config db-mongo-config}))








