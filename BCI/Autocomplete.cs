using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;

namespace BCIKeyboard
{
    public class Autocomplete
    {
        private static Dictionary<string, int> wordFrequencyDictionary = new Dictionary<string, int>();
        private static Dictionary<string, int> inputHistoryDictionary = new Dictionary<string, int>();
        private static int RESULTS_TO_RETURN_WF = 5;
        private static int RESULTS_TO_RETURN_IH = 3;
        private const string WORD_FREQUENCY_FILE_NAME = "dict.wf";
        private static char FREQUENCY_FILE_DELIMITER = ',';

        public static void calculateWordFrequencies(string dataFilePath, string savePath = WORD_FREQUENCY_FILE_NAME)
        {
            wordFrequencyDictionary.Clear();

            StreamReader sr = new StreamReader(dataFilePath);
            string line;

            while ((line = sr.ReadLine()) != null)
            {
                string[] words = line.Split();
                foreach (string word in words)
                {
                    string normalizedWord = removeSpecialCharsFromString(word).ToLower();
                    updateDictionary(normalizedWord);
                }
            }

            saveWordFrequencyInfo(savePath);
        }

        private static void saveWordFrequencyInfo(string savePath)
        {
            StringBuilder sb = new StringBuilder();
            foreach (KeyValuePair<string, int> pair in wordFrequencyDictionary)
            {
                sb.Append(pair.Key + FREQUENCY_FILE_DELIMITER + pair.Value + Environment.NewLine);
            }
            File.WriteAllText(savePath, sb.ToString());
        }

        public static void loadWordFrequencyInfo(string dataFilePath = WORD_FREQUENCY_FILE_NAME)
        {
            wordFrequencyDictionary.Clear();

            foreach (string line in File.ReadLines(dataFilePath))
            {
                string[] parsedLine = line.Trim().Split(FREQUENCY_FILE_DELIMITER);
                string word = parsedLine[0];
                int frequency = Convert.ToInt32(parsedLine[1]);
                wordFrequencyDictionary.Add(word, frequency);
            }
        }

        public static Dictionary<string, int> getWordFrequencies()
        {
            return wordFrequencyDictionary;
        }

        public static List<string> suggestWords(string input)
        {
            Dictionary<string, int> suggestionsBasedOnInputHistory = new Dictionary<string, int>();
            Dictionary<string, int> suggestionsBasedOnWordFrequency = new Dictionary<string, int>();

            suggestionsBasedOnInputHistory = getMatchingWordsFromDictionary(input, inputHistoryDictionary);
            suggestionsBasedOnWordFrequency = getMatchingWordsFromDictionary(input, wordFrequencyDictionary);

            List<string> topIHResults = sortDictionaryByIntegerValue(suggestionsBasedOnInputHistory);
            topIHResults.Reverse();
            List<string> topWFResults = sortDictionaryByIntegerValue(suggestionsBasedOnWordFrequency);
            topWFResults.Reverse();

            List<string> topResults = new List<string>();

            int i = 0;
            while (i < RESULTS_TO_RETURN_IH && i < topIHResults.Count)
            {
                topResults.Add(topIHResults[i]);
                i++;
            }

            i = 0;
            while (i < RESULTS_TO_RETURN_WF && i < topWFResults.Count)
            {
                topResults.Add(topWFResults[i]);
                i++;
            }

            return topResults;
        }

        private static List<string> sortDictionaryByIntegerValue(Dictionary<string, int> dict)
        {
            List<string> sortedDict = (from kv in dict orderby kv.Value select kv.Key).ToList();
            return sortedDict;
        }

        private static Dictionary<string, int> getMatchingWordsFromDictionary(string input, Dictionary<string, int> dict)
        {
            Dictionary<string, int> matchingWordsDictionary = new Dictionary<string, int>();

            if (!String.IsNullOrEmpty(input))
            {
                foreach (KeyValuePair<string, int> temp in dict)
                {
                    if (temp.Key.ToLower().StartsWith(input))
                    {
                        matchingWordsDictionary.Add(temp.Key, temp.Value);
                    }
                }
            }
            return matchingWordsDictionary;
        }

        private static void updateDictionary(string word)
        {
            if (wordFrequencyDictionary.ContainsKey(word))
            {
                wordFrequencyDictionary[word] += 1;
            }
            else
            {
                wordFrequencyDictionary.Add(word, 1);
            }
        }

        private static string removeSpecialCharsFromString(string str)
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.Length; i++)
            {
                bool charIsANumber = str[i] >= '0' && str[i] <= '9';
                bool charIsUppercaseLetter = str[i] >= 'A' && str[i] <= 'Z';
                bool charIsLowercaseLetter = str[i] >= 'a' && str[i] <= 'z';

                if (charIsANumber || charIsUppercaseLetter || charIsLowercaseLetter)
                {
                    sb.Append(str[i]);
                }
            }
            return sb.ToString();
        }

        public static void setInputHistory(Dictionary<string, int> _inputHistoryDictionary)
        {
            inputHistoryDictionary = _inputHistoryDictionary;
        }
    }
}