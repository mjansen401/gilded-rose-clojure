(ns gilded-rose.core-spec
(:require [speclj.core :refer :all]
          [gilded-rose.core :refer [update-quality item]]))

(def any-item (partial item "+5 Dexterity Vest"))
(def smelly-cheese (partial item "Aged Brie"))
(def sulfuras (partial item "Sulfuras, Hand of Ragnaros"))
(def backstage-pass (partial item "Backstage passes to a TAFKAL80ETC concert"))

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
              (let [brie (item-after (smelly-cheese 5 5) 10)]
                (should-be-same 15 (:quality brie))))
          (it "can only increase to max 50"
              (let [brie (item-after (smelly-cheese 5 49) 5)]
                (should-be-same 50 (:quality brie)))))

(describe "Sulfuras, Hand Of Ragnaros"
          (it "will keep quality forever"
              (let [item (item-after (sulfuras 5 5) 10)]
                (should-be-same 5 (:quality item)))))


;; - "Backstage passes", like aged brie, increases in quality as it's sell-in
;; value approaches; quality increases by 2 when there are 10 days or less
;; and by 3 when there are 5 days or less but quality drops to 0 after the
;; concert
(describe "Backstage pass,"
          (it "increases quality over time")
          (it "increases quality by 2 the day 10 to 5 before sell-in")
          (it "increases quality by 3 the day 5 to 0 before sell-in")
          (it "quality is 0 after sell-in date"))

;; TODO: Code coverage for clojure?!
