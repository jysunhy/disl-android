##Hey, You, Get O of My Market: Detecting Malicious Apps in Ocial and Alternative Android Markets
---
###[pdf](http://www.csd.uoc.gr/~hy558/papers/mal_apps.pdf) [ppt](http://www.jrmcclurg.com/papers/talk_overview_hey_you_get_off_my_market.pdf)
###Motivation
* Accuracy: we need low false negatives/positives
* Efficiency and Scalability: at 6 seconds per sample, a collection of 200K apps would take over two weeks to analyze, so speed is very important

###Related work
* TaintDroid
* AppFence

###Methods
* Detecting known malware via permission-based behavioral footprinting
  * Filters based on permissions, then analyzes based on behavior
  * Uses a set of 10 known malware families as footprints
* Detecting unknown malware via heuristics-based filtering
  * Filtering based on dynamic code loading/execution and native code use
  * Analysis based on dynamic monitoring of the execution
  * Conformed malware are fed back to step 1
