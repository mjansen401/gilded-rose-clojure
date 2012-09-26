(ns gilded-rose.core)

(defn update-quality [items]
  (map
    (fn[item] (cond
      (and (< (:sell-in item) 0) (= "Backstage passes to a TAFKAL80ETC concert" (:name item)))
        (merge item {:quality 0})
      (and (or (= "+5 Dexterity Vest" (:name item)) (= "Elixir of the Mongoose" (:name item))) (< (:sell-in item) 0))
        (merge item {:quality (- (:quality item) 2)})
      (and (= (:name item) "Backstage passes to a TAFKAL80ETC concert") (>= (:sell-in item) 0) (< (:sell-in item) 5))
        (merge item {:quality (+ 3 (:quality item))})
      (or (= "+5 Dexterity Vest" (:name item)) (= "Elixir of the Mongoose" (:name item)))
        (merge item {:quality (dec (:quality item))})
      (and (= (:name item) "Backstage passes to a TAFKAL80ETC concert") (>= (:sell-in item) 5) (< (:sell-in item) 10))
        (merge item {:quality (+ 2 (:quality item))})
      (= (:name item) "Aged Brie")
        (if (< (:quality item) 50)
          (merge item {:quality (inc (:quality item))})
          item)
      (= (:name item) "Backstage passes to a TAFKAL80ETC concert")
        (merge item {:quality (inc (:quality item))})
      (= 0 (:quality item)) item
      :else item))
  (map (fn [item]
    (cond
      (= "Aged Brie" (:name item))
        (merge item {:sell-in (dec (:sell-in item))})
      (= "Sulfuras, Hand of Ragnaros" (:name item))
        item
      (= "+5 Dexterity Vest" (:name item))
        (merge item {:sell-in (dec (:sell-in item))})
      (= "Backstage passes to a TAFKAL80ETC concert" (:name item))
        (merge item {:sell-in (dec (:sell-in item))})
      (= "Elixir of the Mongoose" (:name item))
        (merge item {:sell-in (dec (:sell-in item))})
      :else (merge item {:sell-in (dec (:sell-in item))})))
  items)))

(defn update-current-inventory []
  [
    {:name "+5 Dexterity Vest" :sell-in 10 :quality 20}
    {:name "Aged Brie" :sell-in 2 :quality 0}
    {:name "Elixir of the Mongoose" :sell-in 5 :quality 7}
    {:name "Sulfuras, Hand of Ragnaros" :sell-in 0 :quality 80}
    {:name "Backstage passes to a TAFKAL80ETC concert" :sell-in 15 :quality 20}
  ])
