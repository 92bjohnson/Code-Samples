#!/usr/bin/env python

import sys
import numpy
import argparse
import math
import helpers

"""
vrp is the origin
vcp is the window
"""

class graphicsSystem:
	def __init__(self, argDictionary):
		self.resolution = [501, 501]
		self.image = numpy.ndarray((self.resolution[0], self.resolution[1],))
		self.fileName = argDictionary['f']
		self.vpLowerX = int(argDictionary['j'])
		self.vpLowerY = int(argDictionary['k'])
		self.vpUpperX = int(argDictionary['o'])
		self.vpUpperY = int(argDictionary['p'])
		self.prpX = float(argDictionary['x'])
		self.prpY = float(argDictionary['y'])
		self.prpZ = float(argDictionary['z'])
		self.vrpX = float(argDictionary['X'])
		self.vrpY = float(argDictionary['Y'])
		self.vrpZ = float(argDictionary['Z'])
		self.vpnX = float(argDictionary['q'])
		self.vpnY = float(argDictionary['r'])
		self.vpnZ = float(argDictionary['w'])
		self.vupX = float(argDictionary['Q'])
		self.vupY = float(argDictionary['R'])
		self.vupZ = float(argDictionary['W'])
		self.uMin = float(argDictionary['u'])
		self.vMin = float(argDictionary['v'])
		self.uMax = float(argDictionary['U'])
		self.vMax = float(argDictionary['V'])
		self.parallelProjection = bool(argDictionary['P'])
		self.frontPlane = float(argDictionary['F'])
		self.backPlane = float(argDictionary['B'])
		self.perspectiveMatrix = self.calculateViewMatrix()
		self.d = self.prpZ / (self.backPlane - self.prpZ) if not self.parallelProjection else 0
		self.imageXScale = (self.vpUpperX + self.vpLowerX) / 2
		self.imageYScale = (self.vpUpperY + self.vpLowerY) / 2
		
	def execute(self):
		smfFile = open(self.fileName, 'r')
		smfFileContents = smfFile.readlines()
		self.executeSMFInstruction(smfFileContents)
		pass
		
	def executeSMFInstruction(self, smfFileContents):
		vertices = list()
		for i in range(0, len(smfFileContents)):
			currentLine = smfFileContents[i].strip()
			instruction = currentLine.split(' ')
			if (currentLine.startswith("v")):
				x = float(instruction[1])
				y = float(instruction[2])
				z = float(instruction[3])
				vertices.append([x, y, z, 1])
			elif (currentLine.startswith("f")):
				v1 = int(instruction[1]) - 1
				v2 = int(instruction[2]) - 1
				v3 = int(instruction[3]) - 1
				self.create3DLine(vertices[v1], vertices[v2])
				self.create3DLine(vertices[v2], vertices[v3])
				self.create3DLine(vertices[v3], vertices[v1])
		helpers.outputImageToFile(self.image, self.resolution[1])
	
	def calculateViewMatrix(self):
		t = numpy.array([	[1, 0, 0, -self.vrpX],
							[0, 1, 0, -self.vrpY],
							[0, 0, 1, -self.vrpZ],
							[0, 0, 0, 1]])
							
		vpn = numpy.array([self.vpnX, self.vpnY, self.vpnZ])
		vup = numpy.array([self.vupX, self.vupY, self.vupZ])
		rz = vpn / numpy.linalg.norm(vpn)
		vupxrz = numpy.cross(vup, rz, axis = 0)
		rx = vupxrz / numpy.linalg.norm(vupxrz)
		ry = numpy.cross(rz, rx, axis = 0)
		r = numpy.array([	[rx[0], rx[1], rx[2], 0],
							[ry[0], ry[1], ry[2], 0],
							[rz[0], rz[1], rz[2], 0],
							[0, 0, 0, 1]])
							
		prp = numpy.array([[self.prpX], [self.prpY], [self.prpZ]])
		
		cw = numpy.array([	[(self.uMax + self.uMin) / 2],
							[(self.vMax + self.vMin) / 2],
							[0]])
							
		dop = numpy.array([	[cw[0] - prp[0]],
							[cw[1] - prp[1]],
							[-prp[2]]])
							
		shx = -dop[0] / dop[2]
		shy = -dop[1] / dop[2]
		sh = numpy.array([	[1, 0, shx, 0],
							[0, 1, shy, 0],
							[0, 0, 1, 0],
							[0, 0, 0, 1]])

		return self.calculateNPar(t, r, sh) if self.parallelProjection else self.calculateNPer(t, r, sh)

	def calculateNPar(self, t, r, sh):					
		tpar = numpy.array([	[1, 0, 0, -(self.uMax + self.uMin) / 2],
								[0, 1, 0, -(self.vMax + self.vMin) / 2],
								[0, 0, 1, -self.frontPlane ],
								[0, 0, 0, 1]])

		spar = numpy.array([	[2 / (self.uMax - self.uMin), 0, 0, 0],
								[0, 2 / (self.vMax - self.vMin), 0, 0],
								[0, 0, 1 / (self.frontPlane - self.backPlane), 0],
								[0, 0, 0, 1]])
								
		result = numpy.dot(t, r)
		result = numpy.dot(sh, result)
		result = numpy.dot(tpar, result)
		result = numpy.dot(spar, result)
		return result

	def calculateNPer(self, t, r, sh):
		tprp = numpy.array([	[1, 0, 0, -self.prpX],
								[0, 1, 0, -self.prpY],
								[0, 0, 1, -self.prpZ],
								[0, 0, 0, 1]])
								
		prpn = self.prpZ
		sper = numpy.array([	[(2 * prpn) /  ((self.uMax - self.uMin) * (prpn - self.backPlane)), 0, 0, 0],
								[0, (2 * prpn) / ((self.vMax - self.vMin) * (prpn - self.backPlane)), 0, 0],
								[0, 0, 1 / (prpn - self.backPlane), 0],
								[0, 0, 0, 1]])
		
		result = numpy.dot(r, t)
		result = numpy.dot(tprp, result)
		result = numpy.dot(sh, result)
		result = numpy.dot(sper, result)
		return result
	
	def create3DLine(self, v1, v2):
		x1, y1, x2, y2 = self.normalize3DPoints(v1, v2)
		helpers.dda(x1, y1, x2, y2, self.image)
		
	def normalize3DPoints(self, v1, v2):
		v1 = numpy.dot(self.perspectiveMatrix, v1)
		v2 = numpy.dot(self.perspectiveMatrix, v2)
		
		z1 = v1[2] if not self.parallelProjection else 1
		z2 = v2[2] if not self.parallelProjection else 1
		x1 = v1[0] / z1 * self.imageXScale
		y1 = v1[1] / z1 * self.imageYScale
		x2 = v2[0] / z2 * self.imageXScale
		y2 = v2[1] / z2 * self.imageYScale
		
		c = self.resolution[0]
		
		x1 += c / 2
		y1 += c / 2
		x2 += c / 2
		y2 += c / 2
		
		if (not self.parallelProjection):
			x1 = c - x1
			x2 = c - x2
		else:
			y1 = c - y1
			y2 = c - y2
		
		return x1, y1, x2, y2

def main(arguments):
	argDictionary = parseArguments(arguments)
	gs = graphicsSystem(argDictionary)
	gs.execute()
	
def parseArguments(args):
	parser = argparse.ArgumentParser(description = 'Bruce Johnson\'s Graphics System')
	parser.add_argument('-f')
	parser.add_argument('-j', default = 0)
	parser.add_argument('-k', default = 0)
	parser.add_argument('-o', default = 500)
	parser.add_argument('-p', default = 500)
	parser.add_argument('-x', default = 0)
	parser.add_argument('-y', default = 0)
	parser.add_argument('-z', default = 1)
	parser.add_argument('-X', default = 0)
	parser.add_argument('-Y', default = 0)
	parser.add_argument('-Z', default = 0)
	parser.add_argument('-q', default = 0)
	parser.add_argument('-r', default = 0)
	parser.add_argument('-w', default = -1)
	parser.add_argument('-Q', default = 0)
	parser.add_argument('-R', default = 1)
	parser.add_argument('-W', default = 0)
	parser.add_argument('-u', default = -.7)
	parser.add_argument('-v', default = -.7)
	parser.add_argument('-U', default = .7)
	parser.add_argument('-V', default = .7)
	parser.add_argument('-P', action = "store_true")
	parser.add_argument('-F', default = .6)
	parser.add_argument('-B', default = -.6)
	arguments, unrecognizedArguments = parser.parse_known_args(args)
	argDictionary = vars(arguments)
	return argDictionary
	
if __name__ == "__main__":
	main(sys.argv)