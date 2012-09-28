(ns gilded-rose.core-spec
  (:require [speclj.core :refer :all]
            [gilded-rose.core :refer [update-quality]]))
(def vest "+5 Dexterity Vest")
(def brie "Aged Brie")
(def elixir "Elixir of the Mongoose")
(def sulfuras "Sulfuras, Hand of Ragnaros")
(def passes "Backstage passes to a TAFKAL80ETC concert")
(def cake "Conjured Mana Cake")

(def inventory
    [
         {:name vest :sell-in 10 :quality 20}
         {:name brie :sell-in 2 :quality 0}
         {:name elixir :sell-in 5 :quality 7}
         {:name sulfuras :sell-in 0 :quality 80}
         {:name passes  :sell-in 15 :quality 20}
         {:name cake :sell-in 3 :quality 6}
       ])

(defn update-quality-n-times [i, n]
    (loop [items i count 0]
          (if (= count n)
                  items
                  (recur (update-quality items) (inc count)))))

(defn item-by-name [item-name items]
    (first (filter (fn[item] (= item-name (:name item))) items)))
(defn updated-sell-in [item-name updated-items]
    (:sell-in (item-by-name item-name updated-items)))

(defn updated-quality [item-name updated-items]
    (:quality (item-by-name item-name updated-items)))

(describe "gilded rose"
            (describe "acceptance tests"
                          (describe "sell_in values"
                                          (it "decreases by 1 after one update for non-legendary items"
                                                      (let [[updated-items] [(update-quality-n-times inventory 1)]]
                                                                (should= 9 (updated-sell-in vest updated-items))
                                                                (should= 1 (updated-sell-in brie updated-items))
                                                                (should= 4 (updated-sell-in elixir updated-items))
                                                                (should= 14 (updated-sell-in passes updated-items))
                                                                  ))

                                          (it "decreases by 2 after two updates for non-legendary items"
                                                      (let [[updated-items] [(update-quality (update-quality-n-times inventory 1))]]
                                                                (should= 8 (updated-sell-in vest updated-items))
                                                                (should= 0 (updated-sell-in brie updated-items))
                                                                (should= 3 (updated-sell-in elixir updated-items))
                                                                (should= 13 (updated-sell-in passes updated-items))
                                                                  ))

                                          (it "stays constant for legendary items"
                                                      (let [[items-updated-once] [(update-quality-n-times inventory 1)]]
                                                                (should= 0 (updated-sell-in sulfuras items-updated-once))

                                                                (should= 0 (updated-sell-in sulfuras (update-quality items-updated-once)))
                                                                  ))
                                        )

                          (describe "quality values"
                                          (describe "normal items"
                                                            (it "decreases the quality by 1"
                                                                          (should= 19 (updated-quality vest (update-quality-n-times inventory 1)))
                                                                          (should= 6 (updated-quality elixir (update-quality-n-times inventory 1))))
                                                            (it "decreases the quality by 2 when item expired"
                                                                          (should= 8 (updated-quality vest (update-quality-n-times inventory 11)))
                                                                          (should= 0 (updated-quality elixir (update-quality-n-times inventory 6))))
                                                          )

                                          (describe "aged brie"
                                                            (it "increases in quality as it ages"
                                                                          (should= 1 (updated-quality brie (update-quality-n-times inventory 1))))
                                                            (it "never gets above 50 quality" 
                                                                          (should= 50 (updated-quality brie (update-quality-n-times inventory 55))))
                                                          )

                                          (describe "sulfuras"
                                                            (it "never decreases in quality"
                                                                          (should= 80 (updated-quality sulfuras (update-quality-n-times inventory 10))))
                                                          )

                                          (describe "passes"
                                                            (it "increases in quality by 1 with more than 10 days to sell"
                                                                          (should= 25 (updated-quality passes (update-quality-n-times inventory 5))))
                                                            (it "increases in quality by 2 with more than 5 days and less than 10 to sell"
                                                                          (should= 35 (updated-quality passes (update-quality-n-times inventory 10))))
                                                            (it "increases in quality by 3 with more than 0 days and less than 5 to sell"
                                                                          (should= 50 (updated-quality passes (update-quality-n-times inventory 15))))
                                                            (it "drops to 0 when expired"
                                                                          (should= 0 (updated-quality passes (update-quality-n-times inventory 16))))
                                                          )
                                        )
                        )
          )

