from PIL import Image

minFolderNum = 256980
maxFolderNum = 257011
minImageNum = 176116
maxImageNum = 176138
tilesPerFolder = 23
zoomLevel = 19

out_image = "full_map_" + str(zoomLevel) + ".png"
blank_image = Image.open("blank.png")
x = 0

for folder in range(minFolderNum-1, maxFolderNum):
	y=0
	imageNum = minImageNum
	for pic in range(0, tilesPerFolder):
		image = Image.open(str(zoomLevel) + "/" + str(folder+1) + "/" + str(imageNum) + ".png")
		imageNum += 1
		blank_image.paste(image, (x, y))
		y+=254
	x+=254

blank_image.save(out_image)