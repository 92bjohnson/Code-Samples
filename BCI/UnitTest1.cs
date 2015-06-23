/*
    Unit tests for the autocomplete module
*/

using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using BCIKeyboard;
using System.IO;
using System.Collections.Generic;

namespace AutocompleteTests
{
    [TestClass]
    public class AutocompleteTests
    {
        [TestMethod]
        public void TestWordSuggestions_InputHistory()
        {
            Dictionary<String, int> testIH = new Dictionary<String, int>();
            testIH.Add("amber", 10);
            testIH.Add("artificially", 99);
            Autocomplete.setInputHistory(testIH);

            List<String> suggestedWords;

            suggestedWords = Autocomplete.suggestWords("c");
            Assert.AreEqual(suggestedWords.Count, 0);

            suggestedWords = Autocomplete.suggestWords("a");
            Assert.AreEqual(suggestedWords[0], "artificially");
            Assert.AreEqual(suggestedWords[1], "amber");

            suggestedWords = Autocomplete.suggestWords("amb");
            Assert.AreEqual(suggestedWords[0], "amber");
        }

        [TestMethod]
        public void TestWFFileCreation()
        {
            var temp = Directory.GetParent(Environment.CurrentDirectory);
            temp = Directory.GetParent(temp.ToString());
            String sampleTextFilePath = temp.ToString() + @"\test.txt";
            String sampleWFFilePath = sampleTextFilePath.Replace(".txt", ".wf");

            Autocomplete.calculateWordFrequencies(sampleTextFilePath, sampleWFFilePath);
            Assert.IsTrue(File.Exists(sampleWFFilePath));
        }

        [TestMethod]
        public void TestLoadWFFileData()
        {
            var temp = Directory.GetParent(Environment.CurrentDirectory);
            temp = Directory.GetParent(temp.ToString());
            string sampleWFFilePath = temp.ToString() + @"\test.wf";

            Autocomplete.loadWordFrequencyInfo(sampleWFFilePath);
            int a = Autocomplete.getWordFrequencies().Count;
            Assert.AreEqual(Autocomplete.getWordFrequencies().Count, 32);
        }

        [TestMethod]
        public void TestWordSuggestions()
        {
            var temp = Directory.GetParent(Environment.CurrentDirectory);
            temp = Directory.GetParent(temp.ToString());
            string sampleWFFilePath = temp.ToString() + @"\test.wf";

            Autocomplete.loadWordFrequencyInfo(sampleWFFilePath);

            List<String> suggestedWords;

            suggestedWords = Autocomplete.suggestWords("t");
            Assert.AreEqual(suggestedWords[0], "the");

            suggestedWords = Autocomplete.suggestWords("w");
            Assert.AreEqual(suggestedWords.Count, 4);

            suggestedWords = Autocomplete.suggestWords("abcdefg");
            Assert.AreEqual(suggestedWords.Count, 0);

            suggestedWords = Autocomplete.suggestWords("do");
            Assert.AreEqual(suggestedWords[0], "downstairs");
            Assert.AreEqual(suggestedWords[1], "does");
        }
    }
}
