(ns gilded-rose.core)

(defn update-quality [items]
  (map
    (fn[item] (cond
      (and (< (:sell-in item) 0) (= "Backstage passes to a TAFKAL80ETC concert" (:name item)))
        (merge item {:quality 0})
      (or (= (:name item) "Aged Brie") (= (:name item) "Backstage passes to a TAFKAL80ETC concert"))
        (if (and (= (:name item) "Backstage passes to a TAFKAL80ETC concert") (>= (:sell-in item) 5) (< (:sell-in item) 10))
          (merge item {:quality (min 50 (inc (inc (:quality item))))})
          (if (and (= (:name item) "Backstage passes to a TAFKAL80ETC concert") (>= (:sell-in item) 0) (< (:sell-in item) 5))
            (merge item {:quality (min 50 (inc (inc (inc (:quality item)))))})
            (if (and (= (:name item) "Aged Brie") (< (:sell-in item) 0) (< (:quality item) 49))
              (update-in item [:quality] (comp inc inc))
              (if (< (:quality item) 50)
                (merge item {:quality (inc (:quality item))})
                item))))
      (< (:sell-in item) 0)
        (if (or (= "Backstage passes to a TAFKAL80ETC concert" (:name item))
                (and (not= "Sulfuras, Hand of Ragnaros" (:name item))
                     (< (:quality item) 3)))
          (merge item {:quality 0})
          (if (not= "Sulfuras, Hand of Ragnaros" (:name item))
            (merge item {:quality (- (:quality item) 2)})
            item))
      (and (not= "Sulfuras, Hand of Ragnaros" (:name item)) (> (:quality item) 0))
        (merge item {:quality (dec (:quality item))})
      :else item))
  (map (fn [item]
      (if (not= "Sulfuras, Hand of Ragnaros" (:name item))
        (merge item {:sell-in (dec (:sell-in item))})
        item))
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
