 (ns gilded-rose.core-test
     (:use midje.sweet)
     (:require [gilded-rose.core :refer [item update-quality]]))

 (fact "For expired brie"
       (let [inventory [(item "Aged Brie" -1 15)]
             result (update-quality inventory)
             updated-item (first result)]
            (fact "The quality increases by 2"
                  (:quality updated-item) => 17)))
