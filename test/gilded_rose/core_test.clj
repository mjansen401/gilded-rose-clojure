 (ns gilded-rose.core-test
     (:use midje.sweet)
     (:require [gilded-rose.core :refer [item update-quality]]))

 (fact "For expired brie"
       (let [inventory [(item "Aged Brie" -1 15)]
             result (update-quality inventory)
             updated-item (first result)]
            (fact "The quality increases by 2"
                  (:quality updated-item) => 17)))

(fact "For expired brie close to or at max quality"
      (let [inventory [(item "Aged Brie" -1 48)
                       (item "Aged Brie" -1 49)
                       (item "Aged Brie" -1 50)]
            result (update-quality inventory)]
        (fact "The quality is 50"
              (map :quality result) => (repeat 3 50))))

 (fact "For items that decrease in quality close to or at 0"
       (let [inventory [(item "Normal" 5 1)
                        (item "Normal" 5 0)
                        (item "Normal" -1 2)
                        (item "Normal" -1 1)
                        (item "Normal" -1 0)]
             result (update-quality inventory)]
         (fact "The quality is 0"
               (map :quality result) => (repeat 5 0))))


(fact "For backstage passes at or close to max quality"
      (let [inventory [(item "Backstage passes to a TAFKAL80ETC concert" 11 50)
                       (item "Backstage passes to a TAFKAL80ETC concert" 11 49)
                       (item "Backstage passes to a TAFKAL80ETC concert" 7  50)
                       (item "Backstage passes to a TAFKAL80ETC concert" 7  49)
                       (item "Backstage passes to a TAFKAL80ETC concert" 7  48)
                       (item "Backstage passes to a TAFKAL80ETC concert" 2  50)
                       (item "Backstage passes to a TAFKAL80ETC concert" 2  49)
                       (item "Backstage passes to a TAFKAL80ETC concert" 2  48)
                       (item "Backstage passes to a TAFKAL80ETC concert" 2  47)]
            result (update-quality inventory)]
        (fact "The quality is 50"
              (map :quality result) => (repeat 9 50))))

(fact "For expired backstage passes"
      (let [inventory [(item "Backstage passes to a TAFKAL80ETC concert" 0 15)
                       (item "Backstage passes to a TAFKAL80ETC concert" -1 15)]
            result (update-quality inventory)]
        (fact "The quality goes to 0"
              (map :quality result) => (repeat 2 0))))
