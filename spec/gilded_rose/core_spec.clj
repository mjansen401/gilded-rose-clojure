(ns gilded-rose.core-spec
(:require [speclj.core :refer :all]
          [gilded-rose.core :refer [update-quality item]]))

(def any-item (partial item "+5 Dexterity Vest"))
(def smelly-cheese (partial "Aged Brie"))

(defn iterate-updated
  "Create a lazy iterator that will contain recursive updates for an item"
  [item]
  (let [update-fn (fn [i] (first (update-quality [i])))]
    (iterate update-fn item)))

(defn item-after
  "Get the item after n days"
  [item days]
  (last (take (+ days 1) (iterate-updated item))))

(describe "a generic item"
          (it "should decrease quality every day"
              (let [item (item-after (any-item 5 8) 3)]
                (should-be-same 5 (:quality item))))
          (it "should decrease with 2 pts after expiry"
              (let [item (item-after (any-item 2 9) 4)]
                (should-be-same 3 (:quality item)))))
(describe "brie"
          (it "increases quality the older it gets"
              (let [brie (item-after (item "Aged Brie" 5 5) 10)]
                (should-be-same 15 (:quality brie))))
          (it "can only increase to max 50"
              (let [brie (item-after (item "Aged Brie" 5 49) 5)]
                (should-be-same 50 (:quality brie)))))
