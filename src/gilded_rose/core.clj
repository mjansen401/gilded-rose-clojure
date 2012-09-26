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

(defn item [item-name, sell-in, quality]
  {:name item-name, :sell-in sell-in, :quality quality})

(defn update-current-inventory[]
  (let [inventory 
    [
      (item "+5 Dexterity Vest" 10 20)
      (item "Aged Brie" 2 0)
      (item "Elixir of the Mongoose" 5 7)
      (item "Sulfuras, Hand of Ragnaros" 0 80)
      (item "Backstage passes to a TAFKAL80ETC concert" 15 20)
    ]]
    (update-quality inventory)
    ))
