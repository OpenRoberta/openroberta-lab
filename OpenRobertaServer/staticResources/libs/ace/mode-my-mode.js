ace.define(
  "ace/mode/my-mode",
  [
    "require",
    "exports",
    "module",
    "ace/lib/oop",
    "ace/mode/text",
    "ace/mode/text_highlight_rules",
    "ace/worker/worker_client",
  ],
  function (require, exports, module) {
    var oop = require("ace/lib/oop");
    var TextMode = require("ace/mode/text").Mode;
    var TextHighlightRules =
      require("ace/mode/text_highlight_rules").TextHighlightRules;
    var MyHighlightRules = function () {
      var controlFlowKeywords =
        "if|else|else if||while|for|for each|break|continue|waitUntil|orWaitFor|wait ms|return";
      var functionNames =
        "sin|cos|tan|asin|acos|atan|exp|square|sqrt|abs|log10|log|randomInt|" +
        "randomFloat|randomItem|floor|ceil|round|isEven|isOdd|isPrime|isWhole|" +
        "isEmpty|isPositive|isNegative|isDivisibleBy|sum|max|min|average|" +
        "median|stddev|size|indexOfFirst|indexOfLast|get|getFromEnd|getFirst|" +
        "getLast|getAndRemove|getAndRemoveFromEnd|getAndRemoveFirst|" +
        "getAndRemoveLast|createListWith|subList|subListFromIndexToLast|" +
        "subListFromIndexToEnd|subListFromFirstToIndex|subListFromFirstToLast|" +
        "subListFromFirstToEnd|subListFromEndToIndex|subListFromEndToEnd|" +
        "subListFromEndToLast|print|createTextWith|constrain|getRGB";
      var keywordMapper = this.createKeywordMapper(
        {
          "keyword.control": controlFlowKeywords,
          "keyword.function": functionNames,
        },
        "identifier",
        true
      );

      this.$rules = {
        start: [
          { token: "comment", regex: "//" },
          { token: "string", regex: '["](?:(?:\\\\.)|(?:[^"\\\\]))*?["]' },
          { token: "constant.numeric", regex: "0[xX][0-9a-fA-F]+\\b" },
          {
            token: "constant.numeric",
            regex: "[+-]?\\d+(?:(?:\\.\\d*)?(?:[eE][+-]?\\d+)?)?\\b",
          },
          {
            token: "keyword.operator",
            regex: "!|%|\\\\|/|\\*|\\-|\\+|~=|==|<>|!=|<=|>=|=|<|>|&&|\\|\\|",
          },
          { token: "punctuation.operator", regex: "\\?|\\:|\\,|\\;|\\." },
          { token: "paren.lparen", regex: "[[({]" },
          { token: "paren.rparen", regex: "[\\])}]" },
          { token: "text", regex: "\\s+" },
          { token: keywordMapper, regex: "[a-zA-Z_$][a-zA-Z0-9_$]*\\b" },
        ],
      };
    };
    oop.inherits(MyHighlightRules, TextHighlightRules);
    var MyMode = function () {
      this.HighlightRules = MyHighlightRules;
    };
    oop.inherits(MyMode, TextMode);
    (function () {
      this.$id = "ace/mode/my-mode";
    }.call(MyMode.prototype));
    exports.Mode = MyMode;
  }
);
