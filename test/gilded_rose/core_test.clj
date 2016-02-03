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
    (is (= [2 3 4 5] (quality-history (smelly-cheese 2 2) 4))))
  (testing "should never increase quality above 50"
    (is (= [48 49 50 50 50] (quality-history (smelly-cheese 2 48) 5)))))

(deftest a-sulfuras
  (testing "should keep the same quality forever"
    (is (= [60 60 60] (quality-history (sulfuras 1 60) 3)))))

(deftest a-backstage-pass
  (testing "should increase value in steps until sell-in and the drop to 0"
    (is (= [1 2 3 5 7 9 11 13 16 19 22 25 28 0 0]
           (quality-history (backstage-pass 12 1) 15)))))
