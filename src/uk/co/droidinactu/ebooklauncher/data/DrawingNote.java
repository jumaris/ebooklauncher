/*
 * Copyright 2012 Andy Aspell-Clark
 *
 * This file is part of eBookLauncher.
 * 
 * eBookLauncher is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * eBookLauncher is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with eBookLauncher. If not, see http://www.gnu.org/licenses/.
 *
 */
package uk.co.droidinactu.ebooklauncher.data;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import android.database.Cursor;

/*

 <?xml version='1.0' encoding='UTF-8' standalone='yes' ?>
 <n0:notepad type="drawing" createDate="1325667051378" xmlns:n0="http://www.sony.com/notepad">
   <n0:drawing width="600" height="685">
     <n0:page>
       <n1:svg xml:base="" shape-rendering="optimizeQuality" transform="" xmlns:n1="http://www.w3.org/2000/svg">
         <n1:polyline points="56,123 55,122 55,121 55,124 57,130 61,139 65,154 70,170 76,187 91,224 94,229 98,231 " fill="none" stroke="black" fill-rule="nonzero" stroke-linecap="butt" stroke-linejoin="miter" shape-rendering="geometricPrecision" stroke-width="2.5" />
         <n1:polyline points="19,148 21,147 27,145 37,140 50,129 64,117 101,91 113,84 116,83 " fill="none" stroke="black" fill-rule="nonzero" stroke-linecap="butt" stroke-linejoin="miter" shape-rendering="geometricPrecision" stroke-width="2.5" />
         <n1:polyline points="102,163 104,163 107,161 113,158 119,154 126,146 134,138 140,129 143,122 142,117 140,119 138,122 136,129 138,135 143,139 150,140 159,136 170,131 182,126 192,123 200,125 206,133 209,143 210,155 210,168 207,179 204,186 201,189 202,185 208,179 214,170 222,155 231,139 255,92 261,76 264,70 264,68 262,68 261,70 260,75 262,83 266,94 271,104 277,114 283,121 289,125 296,124 301,119 305,113 " fill="none" stroke="black" fill-rule="nonzero" stroke-linecap="butt" stroke-linejoin="miter" shape-rendering="geometricPrecision" stroke-width="2.5" />
         <n1:polyline points="224,89 227,85 236,80 250,73 267,65 313,40 " fill="none" stroke="black" fill-rule="nonzero" stroke-linecap="butt" stroke-linejoin="miter" shape-rendering="geometricPrecision" stroke-width="2.5" />
         <n1:polyline points="48,343 46,341 45,340 44,339 45,340 46,343 50,349 54,360 61,374 83,433 98,463 106,478 110,484 111,484 111,478 109,465 107,448 106,433 105,423 107,424 109,430 114,438 122,449 129,457 137,463 145,464 151,459 156,449 161,434 165,421 169,410 172,403 174,400 174,401 174,405 175,412 174,422 176,432 178,441 181,447 185,449 189,445 192,438 193,428 193,418 192,410 190,406 188,404 189,406 190,410 193,413 197,415 201,415 204,413 208,410 210,405 211,397 212,390 213,386 213,384 215,388 218,392 222,397 227,404 231,410 235,413 238,415 240,412 241,404 242,393 242,384 244,377 246,375 251,376 256,378 262,379 267,378 273,375 276,371 279,367 281,363 282,362 282,363 281,366 280,373 281,381 282,389 283,396 287,399 289,398 292,389 294,376 296,358 296,334 294,271 293,265 292,261 289,260 287,265 285,278 285,298 288,323 303,391 315,408 323,412 330,409 339,401 347,389 355,376 359,365 " fill="none" stroke="black" fill-rule="nonzero" stroke-linecap="butt" stroke-linejoin="miter" shape-rendering="geometricPrecision" stroke-width="2.5" />
         <n1:polyline points="177,528 177,530 179,538 183,548 190,561 198,572 206,579 212,581 218,575 223,568 226,563 229,562 233,566 239,572 245,576 251,575 254,570 256,562 255,549 253,534 249,523 247,514 247,508 249,505 251,503 " fill="none" stroke="black" fill-rule="nonzero" stroke-linecap="butt" stroke-linejoin="miter" shape-rendering="geometricPrecision" stroke-width="2.5" />
         <n1:polyline points="287,498 287,499 288,502 291,509 294,520 298,530 301,537 303,540 302,536 300,528 297,519 294,514 294,512 296,511 300,509 307,506 312,500 317,492 321,486 323,481 323,478 324,480 324,486 325,491 326,498 328,506 333,513 337,517 343,517 349,512 354,503 359,489 362,471 364,449 365,399 364,391 363,388 361,392 360,401 362,417 368,434 376,449 385,458 394,459 402,454 407,443 411,435 411,430 409,434 407,440 407,448 412,453 421,454 432,449 443,438 450,428 453,419 " fill="none" stroke="black" fill-rule="nonzero" stroke-linecap="butt" stroke-linejoin="miter" shape-rendering="geometricPrecision" stroke-width="2.5" />
         <n1:polyline points="329,423 328,423 330,422 338,417 351,410 397,379 413,371 " fill="none" stroke="black" fill-rule="nonzero" stroke-linecap="butt" stroke-linejoin="miter" shape-rendering="geometricPrecision" stroke-width="2.5" />
       </n1:svg>
     </n0:page>
   </n0:drawing>
 </n0:notepad>

 */
@Root(name = "drawing")
public class DrawingNote extends Notepad {

	public class Page {
		@Element
		public Svg svg;
	}

	public class PolyLine {
		@Attribute
		public String points;
		@Attribute
		public String fill;
		@Attribute
		public String stroke;
		@Attribute(name = "fill-rule")
		public String fill_rule;
		@Attribute(name = "stroke-linecap")
		public String stroke_linecap;
		@Attribute(name = "stroke-linejoin")
		public String stroke_linejoin;
		@Attribute(name = "shape-rendering")
		public String shape_rendering;
		@Attribute(name = "stroke_width")
		public String stroke_width;
	}

	@Root
	public class Svg {
		@Attribute(required = false)
		public String transform;
		@Attribute(name = "shape-rendering", required = false)
		public String shape_rendering;
		@ElementList
		public List<PolyLine> polyLine;
	}

	@Attribute(required = false)
	public String width;

	@Attribute(required = false)
	public String height;

	@Element
	public Page page;

	public DrawingNote() {
	}

	public DrawingNote(final Cursor memosCursor) {
		super(memosCursor);
	}
}
