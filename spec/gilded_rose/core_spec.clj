(ns gilded-rose.core-spec
(:require [speclj.core :refer :all]
          [gilded-rose.core :refer [update-quality item]]))

(defn any-item [sell-in quality]
  (item "+5 Dexterity Vest" sell-in quality))

(defn iterate-updated
  "Create a lazy iterator that will contain recursive updates for an item"
  [item]
  (let [update-fn (fn [i] (first (update-quality [i])))]
    (iterate update-fn item)))

(defn item-after [item days]
  (last (take (+ days 1) (iterate-updated item))))

(describe "a generic item"
          (it "should decrease quality every day"
              (should-be-same 5
                              (:quality (item-after (any-item 5 8) 3))))
          (it "should decrease with 2 pts after expiry"
              (should-be-same 3
                              (:quality (item-after (any-item 2 9) 4)))))
(describe "brie"
          (it "increases quality the older it gets")
          (it "can only increase to max 50"))
