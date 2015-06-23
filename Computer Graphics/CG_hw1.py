#!/usr/bin/env python

#	Creates an image based on input from a .ps image file.
#	Rotates, translates, and scales lines, and allows for the adjustment of the viewing window.

import sys
import numpy
import argparse
import math

DELIMITER1 = " ";
DELIMITER2 = "\n";

class graphicsSystem:
	def __init__(self, _fileName, _imgScale, _imgRotation, _xTranslation, _yTranslation, _wLeft, _wTop, _wRight, _wBottom):
		self.fileName = _fileName
		self.imgScale = float(_imgScale)
		self.imgRotation = -math.radians(float(_imgRotation))
		self.xTranslation = -float(_xTranslation)
		self.yTranslation = float(_yTranslation)
		self.lbound = int(_wLeft)
		self.rbound = int(_wRight) + 1
		self.tbound = int(_wTop)
		self.bbound = int(_wBottom) + 1
		self.image = numpy.empty((self.rbound - self.lbound, self.bbound - self.tbound,))
		
	def execute(self):
		psFile = open("hw1.ps", 'r')
		psFileContents = psFile.readlines()
		self.executePSInstruction(psFileContents)
		self.outputImageToFile()
		
	def executePSInstruction(self, fileContents):
		for i in range(0, len(fileContents)):
			currentLine = fileContents[i].strip()
			if (currentLine.endswith("Line")):
				x1, y1, x2, y2 = self.parseLineCreationInstruction(currentLine)
				self.createLine(x1, y1, x2, y2)
				
	def parseLineCreationInstruction(self, instruction):
		parameters = instruction.split(' ')
		return [float(parameters[0]), float(parameters[1]),
				float(parameters[2]), float(parameters[3])]
				
	def createLine(self, x1, y1, x2, y2):
		x1, y1, x2, y2 = self.scalePoints(x1, y1, x2, y2)
		x1, y1, x2, y2 = self.rotatePoints(x1, y1, x2, y2)
		x1, y1, x2, y2 = self.translatePoints(x1, y1, x2, y2)
		self.CohenSutherland(x1, y1, x2, y2)
		
	def dda(self, x1, y1, x2, y2):
		dx = x2 - x1
		dy = y2 - y1
		
		steps = abs(dy) if (abs(dx) <= abs(dy)) else abs(dx)
		
		xIncrement = dx / steps
		yIncrement = dy / steps
		
		x = x1
		y = y1
		
		self.setPixelValue(x, y)
		
		for i in range(0, int(steps)):
			x += xIncrement
			y += yIncrement
			self.setPixelValue(x, y)
			
	def CohenSutherland(self, x1, y1, x2, y2):
		pt1BitCode = self.calculateBitCode(x1, y1)
		pt2BitCode = self.calculateBitCode(x2, y2)
		
		loopCount = 0
		maxLoops = 10
		
		while (True):
			if (self.bothPointsInBoundary(pt1BitCode, pt2BitCode)):
				self.dda(x1, y1, x2, y2)
				break;
			elif (self.rejectPoints(pt1BitCode, pt2BitCode)):
				break;
			else:
				pt1 = pt2BitCode if (self.pointIsInsideWindow(pt1BitCode)) else pt1BitCode
				if (pt1[3] == 1):
					x = self.lbound
					y = y1 + (y2 - y1) * (self.lbound - x1) / (x2 - x1)
				elif (pt1[2] == 1):
					x = self.rbound
					y = y1 + (y2 - y1) * (self.rbound - x1) / (x2 - x1)
				elif (pt1[1] == 1):
					x = x1 + (x2 - x1) * (self.bbound - y1) / (y2 - y1)
					y = self.bbound
				elif (pt1[0] == 1):
					x = x1 + (x2 - x1) * (self.tbound - y1) / (y2 - y1)
					y = self.tbound
					
				if (pt1 == pt1BitCode):
					x1 = x
					y1 = y
					pt1BitCode = self.calculateBitCode(x1, y1)
				else:
					x2 = x
					y2 = y
					pt2BitCode = self.calculateBitCode(x2, y2)
					
				if (self.lineIsASinglePoint(x1, y1, x2, y2)):
					x1 += 1

				loopCount += 1
				if (loopCount > maxLoops):
					break;
					
	def lineIsASinglePoint(self, x1, y1, x2, y2):
		if (x1 == x2 and y1 == y2):
			return True
		return False
		
	def bothPointsInBoundary(self, bitCode1, bitCode2):
		if (self.pointIsInsideWindow(bitCode1) and self.pointIsInsideWindow(bitCode2)):
				return True
		return False
		
	def pointIsInsideWindow(self, bitCode):
		for i in range(0, 4):
			if (bitCode[i] == 1):
				return False
		return True
		
	def rejectPoints(self, bitCode1, bitCode2):
		for i in range(0, 4):
			if (bitCode1[i] == 1 and bitCode2[i] == 1):
				return True
		return False

	def calculateBitCode(self, x, y):
		bitValue = [0, 0, 0, 0]
		if (x < self.lbound):
			bitValue[3] = 1
		if (x > self.rbound):
			bitValue[2] = 1
		if (y > self.bbound):
			bitValue[1] = 1
		if (y < self.tbound):
			bitValue[0] = 1
		return bitValue
			
	def reverseCoordinateOrder(self, x1, y1, x2, y2):
		return x2, y2, x1, y1
		
	def setPixelValue(self, x, y):
		try:
			if (x >= self.lbound and y >= self.tbound):
				self.image[int(x)][int(y)] = 1
		except:
			pass
			
	def outputImageToFile(self):
		output = ""
		for i in range(0, len(self.image)):
			output += DELIMITER1.join(str(element) for element in self.image[i]) + DELIMITER2
		output = self.normalizeOutput(output)
		print output

	def normalizeOutput(self, string):
		string = string.replace(" ", "")
		string = string.replace("1.0", "@")
		string = string.replace("0.0", " ")
		return string
		
	def scalePoints(self, x1, y1, x2, y2):
		newX1 = x1 * self.imgScale
		newY1 = y1 * self.imgScale
		newX2 = x2 * self.imgScale
		newY2 = y2 * self.imgScale 
		return newX1, newY1, newX2, newY2
		
	def rotatePoints(self, x1, y1, x2, y2):
		newX1 = x1 * math.cos(self.imgRotation) - y1 * math.sin(self.imgRotation)
		newY1 = x1 * math.sin(self.imgRotation) + y1 * math.cos(self.imgRotation)
		newX2 = x2 * math.cos(self.imgRotation) - y2 * math.sin(self.imgRotation)
		newY2 = x2 * math.sin(self.imgRotation) + y2 * math.cos(self.imgRotation)
		return newX1, newY1, newX2, newY2
		
	def translatePoints(self, x1, y1, x2, y2):
		x1 += self.xTranslation
		x2 += self.xTranslation
		y1 += self.yTranslation
		y2 += self.yTranslation
		return x1, y1, x2, y2

def main(arguments):
	f, s, r, m, n, a, b, c, d = parseArguments(arguments)
	gs = graphicsSystem(f, s, r, m, n, a, b, c, d)
	gs.execute()
	
def parseArguments(args):
	parser = argparse.ArgumentParser(description = 'Bruce Johnson\'s Graphics System')
	parser.add_argument('-f', default = './hw1.ps')
	parser.add_argument('-s', default = 1)
	parser.add_argument('-r', default = 0)
	parser.add_argument('-m', default = 0)
	parser.add_argument('-n', default = 0)
	parser.add_argument('-a', default = 0)
	parser.add_argument('-b', default = 0)
	parser.add_argument('-c', default = 500)
	parser.add_argument('-d', default = 500)
	arguments, unrecognizedArguments = parser.parse_known_args(args)
	argDictionary = vars(arguments)
	fileName = argDictionary['f']
	imgScale = argDictionary['s']
	imgRotation = argDictionary['r']
	imgXTranslation = argDictionary['m']
	imgYTranslation = argDictionary['n']
	wLeft = argDictionary['a']
	wTop = argDictionary['b']
	wRight = argDictionary['c']
	wBottom = argDictionary['d']
	return fileName, imgScale, imgRotation, imgXTranslation, imgYTranslation, wLeft, wTop, wRight, wBottom
	
if __name__ == "__main__":
	main(sys.argv)
