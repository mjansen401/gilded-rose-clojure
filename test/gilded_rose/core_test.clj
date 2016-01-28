(ns gilded-rose.core-test
  (:require [gilded-rose.core :refer :all]
            [clojure.test :refer :all]))

(def any-item (partial item "+5 Dexterity Vest"))
(def smelly-cheese (partial item "Aged Brie"))
(def sulfuras (partial item "Sulfuras, Hand of Ragnaros"))
(def backstage-pass (partial item "Backstage passes to a TAFKAL80ETC concert"))

(defn- iterate-updated [item]
  (let [update-fn (fn [i] (first (update-quality [i])))] (iterate update-fn item)))

(defn- item-after [item days] (last (take (+ days 1) (iterate-updated item))))

(defn- quality-history [item days]
  (map :quality (take days (iterate-updated item))))

(deftest a-generic-item
    (testing "should decrease quality by 1 every day"
      (is (= [5 4 3 2 1] (quality-history (any-item 5 5) 5))))
    (testing "should decrease quality by 2 after sell-in date"
      (is (= [2 1 0 -2 -4 -6] (quality-history (any-item 2 2) 6)))))

(deftest a-smelly-cheese
  (testing "should increase quality the older it gets"
    (is (= [2 3 4 5] (quality-history (smelly-cheese 2 2) 4)))
    (is (= [48 49 50 50 50] (quality-history (smelly-cheese 2 48) 5)))))

(deftest a-sulfuras
  (testing "should keep the same quality forever"
    (is (= [60 60 60] (quality-history (sulfuras 1 60) 3)))))

;; ;; - "Backstage passes", like aged brie, increases in quality as it's sell-in
;; ;; value approaches; quality increases by 2 when there are 10 days or less
;; ;; and by 3 when there are 5 days or less but quality drops to 0 after the
;; ;; concert
;; (describe "Backstage pass,"
;;           (it "increases quality over time")
;;           (it "increases quality by 2 the day 10 to 5 before sell-in")
;;           (it "increases quality by 3 the day 5 to 0 before sell-in")
;;           (it "quality is 0 after sell-in date"))
