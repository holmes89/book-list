(ns book-list.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [book-list.core-test]))

(doo-tests 'book-list.core-test)

