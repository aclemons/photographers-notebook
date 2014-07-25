
/* Drop Tables */

DROP TABLE [CameraLensCombination];
DROP TABLE [Picture];
DROP TABLE [Filmroll];
DROP TABLE [Setting];




/* Create Tables */

CREATE TABLE [CameraLensCombination]
(
	[CameraID] integer,
	[LensID] integer
);


-- For every roll of film, that a user loads into a camera, he can add one of those. Filmrolls will be displayed in the FilmSelectActivity. A Filmroll consists of details as ASA, Make, Name, Load and unload date and development information. Related to a Filmroll are several Pictures.
CREATE TABLE [Filmroll]
(
	[ID] integer NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,
	-- War "Titel"
	[Title] text,
	-- Date of creation of this entry
	-- 
	-- Datatype: Date
	-- 
	-- 
	[CreatedDate] text,
	[InsertedInCamera] text,
	[RemovedFromCamera] text,
	-- TODO: Should we create a link to an equipemt table or copy the camera namefrom there. (Second solution is better, if a camera was deleted; first reduces data doubling).
	-- 
	-- Was: "Kamera"
	-- 
	[Camera] text,
	-- A user defined field to enter maker and type of the roll of film.
	-- 
	-- Was: "Filmbezeichnung"
	[FilmMakerType] text,
	-- Type of Film and emulsion: e.g. slide, negativ, b/w, color...
	[FilmType] text,
	-- Format/size of the film (e.g. 24x36mm, 6x6, 6x9)
	-- 
	[FilmFormat] text,
	-- ASA/ISO of this roll of film
	[ASA] text,
	[SpecialDevelopment1] text,
	[SpecialDevelopment2] text,
	PRIMARY KEY ([ID])
);


-- Every Filmroll is a container for several Pictures. Each Picture contains information such as the Date of the exposure, Shutterspeed and Aperture, Fokus and Measureing Method and maybe a Note do describe the Picture or scene.
CREATE TABLE [Picture]
(
	[ID] integer NOT NULL UNIQUE,
	[PicNumber] integer,
	-- Date: When was this picture taken?
	-- 
	[ExposureDate] text,
	[Lens] text,
	[Aperture] text,
	[ShutterSpeed] text,
	[FocusDistance] text,
	[Filter] text,
	[Makro] text,
	-- TODO: Rename
	-- 
	[FilterVF] text,
	-- TODO: rename
	[MakroVF] text,
	[Flash] text,
	[FlashCorrection] text,
	[ExposureMeasureMethod] text,
	[ExporuseMeasureCorrection] text,
	[Note] text,
	[longitude] text,
	[latitude] text,
	[altitude] text,
	FOREIGN KEY ([ID])
	REFERENCES [Filmroll] ([ID])
);


CREATE TABLE [Setting]
(
	[ID] integer NOT NULL UNIQUE PRIMARY KEY AUTOINCREMENT,
	-- Examples for a Type are: Camera, Lens, Fokus, etc. Basically, everything type of setting, we had a single table for, previously.
	[SettingType] text,
	[SettingName] text,
	[IsDisplayed] integer,
	[IsDefaultSelection] integer,
	PRIMARY KEY ([ID])
);



