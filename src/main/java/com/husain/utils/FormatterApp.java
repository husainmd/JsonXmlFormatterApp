package com.husain.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter.HighlightPainter;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.XMLUnit;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.husain.utils.Beautifier;
import com.husain.utils.FormatterApp;

public class FormatterApp {

	static int widthMultiplier = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	static int heightMultiplier = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	static String previousFind = "";
	static String currentFind = "";
	static int pos = 0;
	static boolean isHishlighted = false;
	static ArrayList<Integer> allPos = new ArrayList<Integer>();
	static ArrayList<Object> allPosTag = new ArrayList<Object>();
	static int posEnterCounter = 1;
	static HighlightPainter painterYellow = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
	static HighlightPainter painterPink = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
	static HighlightPainter painterCyan = new DefaultHighlighter.DefaultHighlightPainter(Color.cyan);
	static HighlightPainter painterOrange = new DefaultHighlighter.DefaultHighlightPainter(Color.ORANGE);
	static JFrame frame = new JFrame("JSon & XML Formatter / Beautifier ~ Husain");
	static JPanel p1;
	static JTextArea jsonOrXML = new JTextArea("", (int) (10 * heightMultiplier / 900),
			(int) (140 * widthMultiplier / 1600));
	static JScrollPane jsonScroll = new JScrollPane(jsonOrXML);
	static JTextArea jsonFormatted = new JTextArea("", (int) (32 * heightMultiplier / 900),
			(int) (140 * widthMultiplier / 1600));
	static JScrollPane jsonFormattedScroll = new JScrollPane(jsonFormatted);
	static JTextField findTf = new JTextField((int) (30 * widthMultiplier / 1600));
	static int findLength;
	static JLabel totalMsgDisplay;
	static ArrayList<Boolean> directionMemory = new ArrayList<Boolean>();
	static Preferences prefs = null;
	static String userUrl = "", userUsername = "", userPassword = "";

	// JSon Compare
	static JLabel msgLabel = new JLabel("Please enter Formatted/Unformatted JSons below to compare : \n");
	static JTextField jsonLabel1 = new JTextField("Unformatted JSon/XML 1", (int) (70 * widthMultiplier / 1600));
	static JTextField jsonLabel2 = new JTextField("Unformatted JSon/XML 2", (int) (70 * widthMultiplier / 1600));

	static JTextArea json1 = new JTextArea("", (int) (38 * heightMultiplier / 900),
			(int) (70 * widthMultiplier / 1600));
	static JScrollPane jsonScroll1 = new JScrollPane(json1);
	static JTextArea json2 = new JTextArea("", (int) (38 * heightMultiplier / 900),
			(int) (70 * widthMultiplier / 1600));
	static JScrollPane jsonScroll2 = new JScrollPane(json2);
	static JTextArea jsonResult = new JTextArea("", (int) (8 * heightMultiplier / 900),
			(int) (140 * widthMultiplier / 1600));

	static int sizeOfJson1 = 0, sizeOfJson2 = 0;
	static String str1 = "", str2 = "";

	/*
	 * static BoundedRangeModel h1; static BoundedRangeModel h2; static
	 * BoundedRangeModel v1; static BoundedRangeModel v2;
	 */

	public static void main(String args[]) throws Exception {
		directionMemory.add(false);
		directionMemory.add(false);
		frame.setLayout(new GridLayout(1, 1));
		p1 = new JPanel();
		jsonFormatted.setEditable(false);

		JButton format = new JButton("Format & Beautify");
		format.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					try {
						resetFindFunctionality();
						jsonFormatted.setFont(new Font("Verdana", 0, 12));
					} catch (Exception e3) {
					}

					String str = jsonOrXML.getText();

					try {
						if (str == null || str.trim().equals("")) {
							throw new Exception("Please enter any JSon/XML to proceed!");
						} else if (str.contains("{") && str.contains("}")) {
							// This is JSon
							jsonFormatted.setText(Beautifier.formatAndBeautifyJSon(str));
						} else if (str.contains("<") && str.contains(">")) {
							// This is XML
							try {
								jsonFormatted.setText(Beautifier.beautifyXML(Beautifier.formatXML(str)));
							} catch (Exception e2) {
								jsonFormatted.setText(Beautifier.formatXML(str));
							}
						} else
							throw new Exception("");

					} catch (Exception e2) {
						if (e2.getMessage().contains("Please enter any JSon/XML to proceed!")) {
							throw e2;
						}
						if (str.contains("<") && str.contains(">")) {
							// This is XML
							try {
								jsonFormatted.setText(Beautifier.beautifyXML(Beautifier.formatXML(str)));
							} catch (Exception e3) {
								jsonFormatted.setText(Beautifier.formatXML(str));
							}
						} else if (str.contains("{") && str.contains("}")) {
							// This is JSon
							jsonFormatted.setText(Beautifier.formatAndBeautifyJSon(str));
						} else {
							throw new Exception("Please enter a valid JSon/XML!");
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Unable to Format and Beautify!\n" + e1.getMessage(),
							"JSon & XML Formatter / Beautifier ~ Husain", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String str = jsonFormatted.getText();
					String saveAs = "";
					if (str != null) {
						if (!str.equalsIgnoreCase("")) {
							saveAs = JOptionPane.showInputDialog("Please enter the JSon/XML Name to save :");
						} else
							throw new Exception("Please Beautify JSon/XML before saving!");
					} else
						throw new Exception("Please Beautify JSon/XML before saving!");

					String downloadFolderTimeStamp = new SimpleDateFormat("MMM_dd_yyyy").format(new Date());
					String folderPath = System.getProperty("user.dir") + "\\Downloads_Formatted_Beautified\\"
							+ downloadFolderTimeStamp + "\\";
					String filePath = "";
					new File(folderPath).mkdirs();
					int choice;
					outerLoop: while (saveAs != null) {
						if (!saveAs.equalsIgnoreCase("")) {
							filePath = saveAs.replace(" ", "_") + ".txt";
							if (new File(folderPath + filePath).exists()) {
								choice = JOptionPane.showConfirmDialog(null,
										"File already present, do you wish to Overwrite?");
								switch (choice) {

								case 0: {
									// Yes
									writeToDisk(str, folderPath + filePath, folderPath);
									break outerLoop;
								}

								case 1: {
									break;
								}

								case 2: {
									break outerLoop;
								}

								default: // Default
									break;
								}
							} else {
								writeToDisk(str, folderPath + filePath, folderPath);
								break outerLoop;
							}
						} else {
							JOptionPane.showMessageDialog(null, "Please enter a Name to save",
									"JSon & XML Formatter / Beautifier ~ Husain", JOptionPane.ERROR_MESSAGE);
						}
						saveAs = JOptionPane.showInputDialog("Please enter the JSon/XML Name to save :");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Unable to Save!\n" + e1.getMessage(),
							"JSon & XML Formatter / Beautifier ~ Husain", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		/*
		 * JButton save = new JButton("Save"); save.addActionListener(new
		 * ActionListener() { public void actionPerformed(ActionEvent e) { try {
		 * String str = jsonOrXML.getText(); String saveAs = ""; if(str!=null) {
		 * if(!str.equalsIgnoreCase("")) { saveAs = JOptionPane.showInputDialog(
		 * "Please enter the JSon/XML Name to save :"); } else throw new
		 * Exception("Please provide XML or JSon to Format, Beautify and Save!"
		 * ); } else throw new Exception(
		 * "Please provide XML or JSon to Format, Beautify and Save!");
		 * 
		 * String downloadFolderTimeStamp = new
		 * SimpleDateFormat("MMM_dd_yyyy").format(new Date()); String folderPath
		 * = System.getProperty("user.dir")+
		 * "\\Downloads_Formatted_Beautified\\"+downloadFolderTimeStamp+"\\";
		 * String filePath = ""; new File(folderPath).mkdirs(); int choice;
		 * outerLoop:while(saveAs!=null) { if(!saveAs.equalsIgnoreCase("")) {
		 * filePath = saveAs.replace(" ", "_") + ".txt"; if(new
		 * File(folderPath+filePath).exists()) { choice =
		 * JOptionPane.showConfirmDialog(null,
		 * "File already present, do you wish to Overwrite?"); switch(choice) {
		 * 
		 * case 0 : { //Yes writeToDisk(str, folderPath+filePath); break
		 * outerLoop; }
		 * 
		 * case 1 : { break; }
		 * 
		 * case 2 : { break outerLoop; }
		 * 
		 * default : //Default break; } } else { writeToDisk(str,
		 * folderPath+filePath); break outerLoop; } } else {
		 * JOptionPane.showMessageDialog(null, "Please enter a Name to save",
		 * "JSon & XML Formatter / Beautifier ~ Husain",
		 * JOptionPane.ERROR_MESSAGE); } saveAs = JOptionPane.showInputDialog(
		 * "Please enter the JSon/XML Name to save :"); } } catch(Exception e1)
		 * { e1.printStackTrace(); JOptionPane.showMessageDialog(null,
		 * "Unable to Save!\n" + e1.getMessage(),
		 * "JSon & XML Formatter / Beautifier ~ Husain",
		 * JOptionPane.ERROR_MESSAGE); } } });
		 */

		JButton clear = new JButton("Clear");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					resetFindFunctionality();
				} catch (Exception e3) {
				}
				try {
					jsonOrXML.setText("");
					jsonFormatted.setText("");
					jsonFormatted.getHighlighter().removeAllHighlights();
					isHishlighted = false;
					allPos.clear();
					allPosTag.clear();
					posEnterCounter = 1;
					previousFind = currentFind;
					currentFind = "";
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		JButton fetch = new JButton("Fetch Response");
		fetch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					fetchResponse();
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error in Fetching Response!\n" + e1.getMessage(),
							"JSon & XML Formatter / Beautifier ~ Husain", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JButton editLogin = new JButton("Edit Login");
		editLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					try {
						prefs = Preferences.userNodeForPackage(FormatterApp.class);
					} catch (Exception e3) {
					}

					final JFrame f = new JFrame("Credentials ~ Husain");
					f.setLayout(new GridLayout(4, 2));

					JPanel p1 = new JPanel();
					JPanel p2 = new JPanel();
					JPanel p3 = new JPanel();
					JPanel p4 = new JPanel();

					JLabel urlLabel = new JLabel("            URL : ");
					JLabel usernameLabel = new JLabel("Username : ");
					JLabel passwordLabel = new JLabel("Password : ");
					final JTextField urlTf = new JTextField(prefs.get("url", "default"), 30);
					final JTextField usernameTf = new JTextField(prefs.get("username", "default"), 30);
					final JPasswordField passwordTf = new JPasswordField(prefs.get("password", "default"), 30);

					JButton save = new JButton("Save");
					save.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								while (true) {
									userUrl = urlTf.getText();
									userUsername = usernameTf.getText();
									userPassword = passwordTf.getText();

									if (userUrl != null && !(userUrl.trim().equalsIgnoreCase(""))
											&& (userUrl.contains("http"))) {
										if (userUsername != null && !(userUsername.trim().equalsIgnoreCase(""))) {
											if (userPassword != null && !(userPassword.trim().equalsIgnoreCase(""))) {
												prefs.put("url", userUrl.trim());
												prefs.put("username", userUsername.trim());
												prefs.put("password", userPassword.trim());
												f.setVisible(false);
												f.dispose();
												break;
											} else {
												throw new Exception("Please enter a Valid Password.");
											}
										} else {
											throw new Exception("Please enter a Valid Username.");
										}
									} else {
										throw new Exception("Please enter a Valid End Point URL.");
									}
								}
							} catch (Exception e7) {
								JOptionPane.showMessageDialog(null, "Error in saving data!\n" + e7.getMessage(),
										"JSon & XML Formatter / Beautifier ~ Husain", JOptionPane.ERROR_MESSAGE);
							}
						}
					});

					JButton cancel = new JButton("Cancel");
					cancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							f.setVisible(false);
							f.dispose();
						}
					});

					p1.add(urlLabel);
					p1.add(urlTf);
					p2.add(usernameLabel);
					p2.add(usernameTf);
					p3.add(passwordLabel);
					p3.add(passwordTf);
					p4.add(save);
					p4.add(cancel);
					f.add(p1);
					f.add(p2);
					f.add(p3);
					f.add(p4);
					int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
					int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
					int width = 500;
					int height = 200;
					int x = (screenWidth - width) / 2;
					int y = (screenHeight - height) / 2;
					f.setBounds(x, y, width, height);
					f.setVisible(true);

				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error in Fetching Response!\n" + e1.getMessage(),
							"JSon & XML Formatter / Beautifier ~ Husain", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		JButton comparator = new JButton("Compare");
		comparator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					final JFrame f = new JFrame("JSon Compare ~ Husain");
					JPanel p1 = new JPanel();
					JButton compare = new JButton("Compare");

					/*
					 * h1 = jsonScroll1.getHorizontalScrollBar().getModel(); h2
					 * = jsonScroll2.getHorizontalScrollBar().getModel(); v1 =
					 * jsonScroll1.getVerticalScrollBar().getModel(); v2 =
					 * jsonScroll2.getVerticalScrollBar().getModel();
					 */

					json1.addMouseListener(new MouseListener() {
						public void mouseEntered(MouseEvent e) {
							jsonScroll2.getHorizontalScrollBar()
									.setModel(jsonScroll1.getHorizontalScrollBar().getModel());
							jsonScroll2.getVerticalScrollBar().setModel(jsonScroll1.getVerticalScrollBar().getModel());
						}

						public void mouseClicked(MouseEvent e) {
						}

						public void mousePressed(MouseEvent e) {
						}

						public void mouseReleased(MouseEvent e) {
						}

						public void mouseExited(MouseEvent e) {
						}
					});

					json2.addMouseListener(new MouseListener() {
						public void mouseEntered(MouseEvent e) {
							// TODO Auto-generated method stub
							jsonScroll1.getHorizontalScrollBar()
									.setModel(jsonScroll2.getHorizontalScrollBar().getModel());
							jsonScroll1.getVerticalScrollBar().setModel(jsonScroll2.getVerticalScrollBar().getModel());
						}

						public void mouseClicked(MouseEvent e) {
						}

						public void mousePressed(MouseEvent e) {
						}

						public void mouseReleased(MouseEvent e) {
						}

						public void mouseExited(MouseEvent e) {
						}
					});

					compare.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								String jsonStr1 = json1.getText();
								String jsonStr2 = json2.getText();

								String formatted1 = "";
								String formatted2 = "";

								if (jsonStr1 != null && jsonStr2 != null) {
									if (!(jsonStr1.trim().equals("")) && !(jsonStr2.trim().equals(""))
											&& (jsonStr1.contains("{")) && (jsonStr1.contains("}"))
											&& (jsonStr2.contains("{")) && (jsonStr2.contains("}"))) {
										// This is JSon

										formatted1 = Beautifier.formatAndBeautifyJSon(jsonStr1);
										formatted2 = Beautifier.formatAndBeautifyJSon(jsonStr2);

										// Make the no of lines in both cases
										// same

										sizeOfJson1 = formatted1.split("\n").length;
										sizeOfJson2 = formatted2.split("\n").length;

										StringBuffer sbuf1 = new StringBuffer(formatted1);
										StringBuffer sbuf2 = new StringBuffer(formatted2);

										if (sizeOfJson1 > sizeOfJson2) {
											while (sizeOfJson1 != sizeOfJson2) {
												sbuf2.append("\n");
												sizeOfJson2++;
											}
										} else if (sizeOfJson1 < sizeOfJson2) {
											while (sizeOfJson1 != sizeOfJson2) {
												sbuf1.append("\n");
												sizeOfJson1++;
											}
										}

										formatted1 = sbuf1.toString();
										formatted2 = sbuf2.toString();

										json1.setText(formatted1);
										json2.setText(formatted2);

										try {
											JSONAssert.assertEquals(formatted1, formatted2, JSONCompareMode.STRICT);
											JOptionPane.showMessageDialog(null,
													"Both the JSons are Perfectly matching!",
													"JSon Compare ~ Husain", JOptionPane.INFORMATION_MESSAGE);
										} catch (AssertionError e1) {
											e1.printStackTrace();
											String[] allLines = e1.getMessage().split("\n");
											ArrayList<String> expected = new ArrayList<String>();
											ArrayList<String> expectedAndGot = new ArrayList<String>();
											ArrayList<String> expectedButNotFound = new ArrayList<String>();
											ArrayList<String> unexpected = new ArrayList<String>();
											/**/

											try {
												for (int i = 0; i < allLines.length; i++) {
													if (allLines[i].contains("Expected: ")) {
														String parameter = "";
														if (allLines[i - 1].contains(".")) {
															String[] arr = allLines[i - 1].split("\\.");
															parameter = arr[arr.length - 1];
														} else {
															parameter = allLines[i - 1];
														}
														if (allLines[i + 1].contains("got: ")) {
															expected.add("\"" + parameter + "\":\""+ allLines[i].replace("Expected: ", "").trim()+ "\"");
															expected.add("\"" + parameter + "\":"+ allLines[i].replace("Expected: ", "").trim());
															expectedAndGot.add("\"" + parameter + "\":\""+ allLines[i + 1].replace("got: ", "").trim()+ "\"");
															expectedAndGot.add("\"" + parameter + "\":"+ allLines[i + 1].replace("got: ", "").trim());
														} else if (allLines[i + 1].contains("but none found")) {
															expectedButNotFound
																	.add(allLines[i].replace("Expected: ", "").trim());
														}
													} else if (allLines[i].contains("Unexpected: ")) {
														unexpected.add(allLines[i].replace("Unexpected: ", "").trim());
													}
												}
											} catch (Exception e12) {
												e12.printStackTrace();
												expected.removeAll(expected);
												expectedAndGot.removeAll(expectedAndGot);
												expectedButNotFound.removeAll(expectedButNotFound);
												unexpected.removeAll(unexpected);
												for (int i = 0; i < allLines.length; i++) {
													if (allLines[i].contains("Expected: ")) {

														if (allLines[i + 1].contains("got: ")) {
															expected.add(allLines[i].replace("Expected: ", "").trim());
															expectedAndGot
																	.add(allLines[i + 1].replace("got: ", "").trim());
														} else if (allLines[i + 1].contains("but none found")) {
															expectedButNotFound
																	.add(allLines[i].replace("Expected: ", "").trim());
														}
													} else if (allLines[i].contains("Unexpected: ")) {
														unexpected.add(allLines[i].replace("Unexpected: ", "").trim());
													}
												}
											}

											System.out.println(expected);
											System.out.println(expectedAndGot);
											System.out.println(expectedButNotFound);
											System.out.println(unexpected);

											for (String a : expected) {
												highlightWord(json1, a, painterYellow);
											}

											for (String a : expectedAndGot) {
												highlightWord(json2, a, painterYellow);
											}

											for (String a : expectedButNotFound) {
												highlightWord(json1, a, painterPink);
											}

											for (String a : unexpected) {
												highlightWord(json2, a, painterCyan);
											}

											jsonResult.setText(e1.getMessage());
											
											
											
											
											//22nd Nov 2017
											/*ArrayList<String> allLines1 = new ArrayList<String>();
											ArrayList<String> allLines2 = new ArrayList<String>();
											
											for(String a : formatted1.split("\n"))
												allLines1.add(a);
											
											for(String a : formatted2.split("\n"))
												allLines2.add(a);
											
											if(allLines1.size()>allLines2.size())
											{
												while(allLines2.size()!=allLines1.size())
												{
													allLines2.add("");
												}
											}
											else if(allLines1.size()<allLines2.size())
											{
												while(allLines2.size()!=allLines1.size())
												{
													allLines1.add("");
												}
											}
											
											int runSize = allLines1.size();
											
											for(int i=0; i<runSize; i++)
											{
												String lhs = allLines1.get(i).trim();
												String rhs = allLines2.get(i).trim();
												
												if(isStringPartialValueOfAnyArraylistElement(lhs, expectedButNotFound))
												{
													if(!isStringPartialValueOfAnyArraylistElement(rhs, expectedButNotFound))
													{
														System.out.println("++++++++++++++++++++++++++ Added blank line in al2");
														allLines2.add(i, "");
													}
												}
												
												if(isStringPartialValueOfAnyArraylistElement(rhs, unexpected))
												{
													if(!isStringPartialValueOfAnyArraylistElement(lhs, unexpected))
													{
														System.out.println("++++++++++++++++++++++++++ Added blank line in al1");
														allLines1.add(i, "");
													}
												}
											}
											
											if(allLines1.size()>allLines2.size())
											{
												while(allLines2.size()!=allLines1.size())
												{
													allLines2.add("");
												}
											}
											else if(allLines1.size()<allLines2.size())
											{
												while(allLines2.size()!=allLines1.size())
												{
													allLines1.add("");
												}
											}
											
											StringBuffer sbuf11 = new StringBuffer();
											StringBuffer sbuf22 = new StringBuffer();
											
											for(String a : allLines1)
											{
												sbuf11.append(a+"\n");
											}
											
											for(String a : allLines2)
											{
												sbuf22.append(a+"\n");
											}
											
											
											
											json1.setText(sbuf11.toString());
											json2.setText(sbuf22.toString());
											
											for (String a : expected) {
												highlightWord(json1, a, painterYellow);
											}

											for (String a : expectedAndGot) {
												highlightWord(json2, a, painterYellow);
											}

											for (String a : expectedButNotFound) {
												highlightWord(json1, a, painterPink);
											}

											for (String a : unexpected) {
												highlightWord(json2, a, painterCyan);
											}

											jsonResult.setText(e1.getMessage());*/
										}
									} else if (!(jsonStr1.trim().equals("")) && !(jsonStr2.trim().equals(""))
											&& !(jsonStr1.contains("{")) && !(jsonStr1.contains("}"))
											&& !(jsonStr2.contains("{")) && !(jsonStr2.contains("}"))
											&& (jsonStr1.contains("<")) && (jsonStr1.contains(">"))
											&& (jsonStr2.contains("<")) && (jsonStr2.contains(">"))) {
										// This is XML

										System.out.println("This is XML comparison");
										formatted1 = Beautifier.beautifyXML(Beautifier.formatXML(jsonStr1));
										formatted2 = Beautifier.beautifyXML(Beautifier.formatXML(jsonStr2));

										// Make the no of lines in both cases
										// same

										sizeOfJson1 = formatted1.split("\n").length;
										sizeOfJson2 = formatted2.split("\n").length;

										StringBuffer sbuf1 = new StringBuffer(formatted1);
										StringBuffer sbuf2 = new StringBuffer(formatted2);

										if (sizeOfJson1 > sizeOfJson2) {
											while (sizeOfJson1 != sizeOfJson2) {
												sbuf2.append("\n");
												sizeOfJson2++;
											}
										} else if (sizeOfJson1 < sizeOfJson2) {
											while (sizeOfJson1 != sizeOfJson2) {
												sbuf1.append("\n");
												sizeOfJson1++;
											}
										}
										formatted1 = sbuf1.toString();
										formatted2 = sbuf2.toString();

										json1.setText(formatted1);
										json2.setText(formatted2);

										// XML Comparison

										XMLUnit.setIgnoreWhitespace(true);
										XMLUnit.setIgnoreAttributeOrder(true);
										DetailedDiff diff = new DetailedDiff(
												XMLUnit.compareXML(formatted1, formatted2));
										List<?> allDifferences = diff.getAllDifferences();

										if (allDifferences.size() == 0) {
											throw new Exception("Both the XMLs match perfectly!");
										}

										ArrayList<String> expected = new ArrayList<String>();
										ArrayList<String> expectedAndGot = new ArrayList<String>();
										ArrayList<String> nodesMismatch = new ArrayList<String>();
										ArrayList<String> expectedButNotFound = new ArrayList<String>();
										ArrayList<String> unexpected = new ArrayList<String>();
										String msg = "";
										for (Object a : allDifferences) {
											msg = a.toString();
											System.out.println(msg);

											if (msg.contains("Expected text value") && msg.contains("but was")) {
												// Value Change in XML1 and XML2
												// Expected text value 'SAINT
												// LOUIS' but was 'SAIN LOUIS' -
												// comparing <city ...>SAINT
												// LOUIS</city> at
												// /ConfigRequest[1]/header[1]/partnerInfo[1]/billingAddress[1]/city[1]/text()[1]
												// to <city ...>SAIN
												// LOUIS</city> at
												// /ConfigRequest[1]/header[1]/partnerInfo[1]/billingAddress[1]/city[1]/text()[1]
												msg = msg.replace("Expected text value '", "");
												// SAINT LOUIS' but was 'SAIN
												// LOUIS' - comparing <city
												// ...>SAINT LOUIS</city> at
												// /ConfigRequest[1]/header[1]/partnerInfo[1]/billingAddress[1]/city[1]/text()[1]
												// to <city ...>SAIN
												// LOUIS</city> at
												// /ConfigRequest[1]/header[1]/partnerInfo[1]/billingAddress[1]/city[1]/text()[1]
												String[] expectedStr = msg.split("' but was '");
												// 0 : SAINT LOUIS
												// 1 : SAIN LOUIS' - comparing
												// <city ...>SAINT LOUIS</city>
												// at
												// /ConfigRequest[1]/header[1]/partnerInfo[1]/billingAddress[1]/city[1]/text()[1]
												// to <city ...>SAIN
												// LOUIS</city> at
												// /ConfigRequest[1]/header[1]/partnerInfo[1]/billingAddress[1]/city[1]/text()[1]

												// String ex1 = expectedStr[0];
												// String got2 =
												// expectedStr[1].split("' -
												// comparing ")[0];
												String compareParameter = expectedStr[1].split("' - comparing <")[1]
														.split("...>")[0].trim();
												expected.add("<" + compareParameter + ">" + expectedStr[0] + "</"
														+ compareParameter + ">");
												expectedAndGot.add("<" + compareParameter + ">"
														+ expectedStr[1].split("' - comparing ")[0] + "</"
														+ compareParameter + ">");
											} else if (msg.contains("number of child nodes")) {
												// Nodes mismatch between XML1
												// and XML2, so highlight the
												// Parent Node
												String str = msg.split(" - comparing ")[1].split("...> at ")[0] + ">";
												nodesMismatch.add(str);
											}

											else if (msg.contains("Expected presence of child node")
													&& msg.contains("but was 'null'")) {
												// Present in XML1 but absent in
												// XML2
												// Expected presence of child
												// node
												// '{urn:cisco:icw:config:common}userId'
												// but was 'null' - comparing
												// <userId...> at
												// /ConfigRequest[1]/header[1]/partnerInfo[1]/userId[1]
												// to at null

												String str = msg.split(" - comparing ")[1].split(" at ")[0]
														.replace("...>", "");
												expectedButNotFound.add(str);

											} else if (msg.contains("Expected presence of child node 'null'")
													&& msg.contains("but was")) {
												// Absent in XML1 but present in
												// XML2
												// Expected presence of child
												// node 'null' but was
												// '{urn:cisco:icw:config:common}state'
												// - comparing at null to
												// <state...> at
												// /ConfigRequest[1]/header[1]/endCustomerSite[1]/state[1]
												//
												String str = msg.split(" - comparing  at null to ")[1].split(" at ")[0]
														.replace("...>", "");
												unexpected.add(str);
											}
										}

										System.out.println(expected);
										System.out.println(expectedAndGot);

										System.out.println(expectedButNotFound);
										System.out.println(unexpected);

										System.out.println(nodesMismatch);

										// logic to add blank line for Expected
										// but not found

										ArrayList<String> al1 = new ArrayList<String>();
										ArrayList<String> al2 = new ArrayList<String>();

										for (String a : formatted1.split("\n"))
											al1.add(a);

										for (String a : formatted2.split("\n"))
											al2.add(a);

										int loopSize = 0;

										if (al1.size() > al2.size()) {
											while (al1.size() != al2.size()) {
												System.out.println("adding in al2");
												al2.add("");
											}
										} else if (al1.size() < al2.size()) {
											while (al1.size() != al2.size()) {
												System.out.println("adding in al1");
												al1.add("");
											}
										}

										loopSize = al1.size();

										System.out.println("Loopsize ========================" + loopSize);

										System.out.println("expectedButNotFound ========================"
												+ expectedButNotFound.size());

										for (int i = 0; i < loopSize; i++) {
											if (isStringPartialValueOfAnyArraylistElement(al1.get(i), expectedButNotFound)
													&& !isStringPartialValueOfAnyArraylistElement(al2.get(i),
															expectedButNotFound)) {
												System.out.println(
														"==================================== Expected but not found : "
																+ al1.get(i));
												String expectedButNotFoundStr = getTagValue(al1.get(i),
														expectedButNotFound);
												while (true) {
													al2.add(i, "");
													if (al1.get(i).contains("</" + expectedButNotFoundStr
															.replace("<", "").replace(">", ""))) {
														break;
													}
													i++;
												}
											} else if (!isStringPartialValueOfAnyArraylistElement(al1.get(i), unexpected)
													&& isStringPartialValueOfAnyArraylistElement(al2.get(i), unexpected)) {
												System.out.println("==================================== Unexpected : "
														+ al2.get(i));
												String unexpectedStr = getTagValue(al2.get(i), unexpected);
												while (true) {
													al1.add(i, "");
													if (al2.get(i).contains(
															"</" + unexpectedStr.replace("<", "").replace(">", ""))) {
														break;
													}
													i++;
												}
											}
										}

										if (al1.size() > al2.size()) {
											while (al1.size() != al2.size()) {
												System.out.println("wrong___________adding in al2");
												al2.add("");
											}
										} else if (al1.size() < al2.size()) {
											while (al1.size() != al2.size()) {
												System.out.println("wrong___________adding in al1");
												al1.add("");
											}
										}

										StringBuffer s1 = new StringBuffer();
										StringBuffer s2 = new StringBuffer();

										for (String a : al1) {
											s1.append(a + "\n");
										}

										for (String a : al2) {
											s2.append(a + "\n");
										}

										json1.setText(s1.toString());
										json2.setText(s2.toString());

										for (int i = 0; i < al1.size(); i++) {
											if ((al1.get(i).trim() == null || al1.get(i).trim() == "")
													&& (al2.get(i).trim() != null && al2.get(i).trim() != "")) {
												// highlightUnexpected.add(al2.get(i));
												highlightWord(json2, al2.get(i), painterCyan);
											} else if ((al1.get(i).trim() != null && al1.get(i).trim() != "")
													&& (al2.get(i).trim() == null || al2.get(i).trim() == "")) {
												// highlightExpectedButNotFound.add(al1.get(i));
												highlightWord(json1, al1.get(i), painterPink);
											}
										}

										for (String a : expected) {
											highlightWord(json1, a, painterYellow);
										}

										for (String a : expectedAndGot) {
											highlightWord(json2, a, painterYellow);
										}

										/*
										 * for(String a : expectedButNotFound) {
										 * highlightWord(json1, a, painterPink);
										 * } for(String a : unexpected) {
										 * highlightWord(json2, a, painterCyan);
										 * }
										 */

										for (String a : nodesMismatch) {
											highlightWord(json2, a, painterOrange);
											highlightWord(json1, a, painterOrange);
										}

										StringBuffer sss = new StringBuffer();
										for (Object a : allDifferences) {
											sss.append(a.toString() + "\n");
										}

										jsonResult.setText(sss.toString());
									} else {
										throw new Exception("Please enter Valid JSon or XML");
									}
								} else {
									throw new Exception("Please enter JSon1 and JSon2.");
								}

							} catch (Exception e3) {
								e3.printStackTrace();
								// JOptionPane.showMessageDialog(null, "Error in
								// Comparing JSon!\n" + e3.getMessage(), "JSon
								// Compare ~ Husain",
								// JOptionPane.ERROR_MESSAGE);
								JOptionPane.showMessageDialog(null, e3.getMessage(), "JSon Compare ~ Husain",
										JOptionPane.ERROR_MESSAGE);
							}
						}

						public boolean isStringPartialValueOfAnyArraylistElement(String str, ArrayList<String> arr) {
							for (String a : arr) {
								if (str.contains(a))
									return true;
							}
							return false;
						}

						public String getTagValue(String str, ArrayList<String> arr) throws Exception {
							for (String a : arr) {
								if (str.contains(a))
									return a;
							}
							throw new Exception("some error occured, tag value is not found");
						}

						public void highlightWord(JTextArea a, String word, HighlightPainter highLightPainter)
								throws Exception {
							Document document = a.getDocument();
							int pos = 0;
							while (pos + word.length() <= document.getLength()) {
								String match = document.getText(pos, word.length()).toLowerCase();
								if (match.equalsIgnoreCase(word)) {
									a.getHighlighter().addHighlight(pos, pos + word.length(), highLightPainter);
									System.out.println("Added highlight");
								}
								pos++;
							}

						}
					});

					JButton clear = new JButton("Clear");
					clear.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								json1.setText("");
								json2.setText("");
								jsonResult.setText("");
							} catch (Exception e1) {
							}
						}
					});

					JButton exit = new JButton("Exit");
					exit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								json1.setText("");
								json2.setText("");
								jsonResult.setText("");
								f.setVisible(false);
								f.dispose();
							} catch (Exception e1) {
							}
						}
					});

					/*
					 * JComboBox<String> scrollFreeze = new
					 * JComboBox<String>(new String[] {
					 * "Simultaneous Scroll - ON", "Simultaneous Scroll - OFF"
					 * }); scrollFreeze.setSelectedItem(
					 * "Simultaneous Scroll - ON");
					 * 
					 * scrollFreeze.addActionListener(new ActionListener() {
					 * 
					 * @Override public void actionPerformed(ActionEvent e) {
					 * if(scrollFreeze.getSelectedItem().toString().
					 * equalsIgnoreCase("Simultaneous Scroll - ON")) {
					 * jsonScroll1.getHorizontalScrollBar().setModel(jsonScroll2
					 * .getHorizontalScrollBar().getModel());
					 * jsonScroll1.getVerticalScrollBar().setModel(jsonScroll2.
					 * getVerticalScrollBar().getModel());
					 * jsonScroll2.getHorizontalScrollBar().setModel(jsonScroll1
					 * .getHorizontalScrollBar().getModel());
					 * jsonScroll2.getVerticalScrollBar().setModel(jsonScroll1.
					 * getVerticalScrollBar().getModel()); } else {
					 * jsonScroll1.getHorizontalScrollBar().setModel(h1);
					 * jsonScroll1.getVerticalScrollBar().setModel(v1);
					 * jsonScroll2.getHorizontalScrollBar().setModel(h2);
					 * jsonScroll2.getVerticalScrollBar().setModel(v2); } } });
					 */

					jsonResult.setEditable(false);
					JScrollPane jsonScrollResult = new JScrollPane(jsonResult);
					p1.add(jsonLabel1);
					p1.add(jsonLabel2);
					p1.add(jsonScroll1);
					p1.add(jsonScroll2);
					p1.add(compare);
					// p1.add(scrollFreeze);
					p1.add(clear);
					p1.add(exit);
					p1.add(jsonScrollResult);
					f.add(p1);
					f.setVisible(true);
					f.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
					// f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error in Comparing JSon!\n" + e1.getMessage(),
							"JSon Compare ~ Husain", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JButton exit = new JButton("Exit");
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					frame.setVisible(false);
					frame.dispose();
					System.exit(0);
				} catch (Exception e1) {
				}
			}
		});

		JLabel findInPage = new JLabel("Find in Result : ");

		findTf.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt) {
				int code = evt.getKeyCode();
				if (code == KeyEvent.VK_ENTER) {
					try {
						uponPressingEnter();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (code == KeyEvent.VK_ESCAPE) {
					resetFindFunctionality();
				}
			}
		});

		JButton previousSearch = new JButton("Previous Match");
		previousSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				highlightPrevious();
			}
		});

		JButton nextSearch = new JButton("Next Match");
		nextSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				directionMemory.add(0, true);
				directionMemory.remove(2);
				highlightNext();
			}
		});

		totalMsgDisplay = new JLabel("");
		p1.add(new JLabel("Please enter XML or JSon to Format and Beautify :"));
		p1.add(jsonScroll);
		p1.add(format);
		p1.add(save);
		p1.add(clear);
		p1.add(fetch);
		p1.add(editLogin);
		p1.add(comparator);
		p1.add(exit);
		p1.add(jsonFormattedScroll);
		p1.add(findInPage);
		p1.add(findTf);
		p1.add(previousSearch);
		p1.add(nextSearch);
		p1.add(totalMsgDisplay);
		frame.add(p1);
		frame.setVisible(true);
		frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void fetchResponse() throws Exception {
		try {
			userUrl = prefs.get("url", "default");
			userUsername = prefs.get("username", "default");
			userPassword = prefs.get("password", "default");
		} catch (Exception e2) {
			userUrl = null;
			userUsername = null;
			userPassword = null;
		}

		String request = jsonOrXML.getText();
		System.out.println(request);

		if (request == null)
			throw new Exception("Please enter the Request");
		else if (request.trim().equalsIgnoreCase(""))
			throw new Exception("Please enter the Request");

		if (userUrl != null && !(userUrl.trim().equalsIgnoreCase(""))
				&& !(userUrl.trim().equalsIgnoreCase("default"))) {
			if (userUsername != null && !(userUsername.trim().equalsIgnoreCase(""))
					&& !(userUsername.trim().equalsIgnoreCase("default"))) {
				if (userPassword != null && !(userPassword.trim().equalsIgnoreCase(""))
						&& !(userPassword.trim().equalsIgnoreCase("default"))) {
					if (request.contains("<") && request.contains(">")) {
						// This is XML, do nothing
					} else {
						// This is JSon, fetch response
						request = Beautifier.formatAndBeautifyJSon(request);

						System.out.println("\n\n" + request + "\n\n");
						final String username = userUsername;
						final String password = userPassword;
						String requestUrl = userUrl; // "https://ibpm-stage.cisco.com/cvc-qa2/sdg/PRRestService/SalesDiscountGuidance/DiscountGuidance/GetGuidanceByQuoteLines";

						// Authentication for getting response
						Authenticator.setDefault(new Authenticator() {
							@Override
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(username, password.toCharArray());
							}
						});

						String jsonResponse = sendPostRequest(requestUrl, request);
						jsonFormatted.setFont(new Font("Verdana", 0, 12));
						jsonFormatted.setText(Beautifier.formatAndBeautifyJSon(jsonResponse));
					}
				} else {
					throw new Exception("Please Save a valid Password through 'Edit Login' button");
				}
			} else {
				throw new Exception("Please Save a valid Username through 'Edit Login' button");
			}
		} else {
			throw new Exception("Please Save a valid End Point URL through 'Edit Login' button");
		}
	}

	public static void writeToDisk(String str, String filePath, String folderPath) throws Exception {
		PrintWriter writer = new PrintWriter(filePath, "UTF-8");

		for (String a : str.split("\n")) {
			writer.println(a);
		}
		writer.close();

		int choice = JOptionPane.showConfirmDialog(null,
				"Saved successfully!\n\nDo you wish to Open the containing Folder?");
		switch (choice) {
		case 0: {
			// Yes
			try {
				Runtime.getRuntime().exec("explorer.exe /select," + filePath);
			} catch (Exception e) {
			}
			break;
		}

		case 1: {
			// No
			break;
		}

		case 2: {
			// Cancel
			break;
		}

		default: {
			break;
		}
		}
	}

	public static void resetFindFunctionality() {
		findTf.setText("");
		previousFind = currentFind;
		currentFind = "";
		resetEverything();
	}

	public static void resetEverything() {
		jsonFormatted.getHighlighter().removeAllHighlights();
		isHishlighted = false;
		allPos.clear();
		allPosTag.clear();
		posEnterCounter = 1;
		totalMsgDisplay.setText("");
		pos = 0;
	}

	public static void highlightNext() {
		if (posEnterCounter != 1) {
			if (!directionMemory.get(1))
				posEnterCounter++;
		}
		try {
			jsonFormatted.getHighlighter().removeHighlight(allPosTag.get(posEnterCounter));
			allPosTag.remove(allPosTag.get(posEnterCounter));
			allPosTag.add(posEnterCounter, jsonFormatted.getHighlighter().addHighlight(allPos.get(posEnterCounter),
					allPos.get(posEnterCounter) + findLength, painterPink));
			Rectangle viewRect = jsonFormatted.modelToView(allPos.get(posEnterCounter));
			jsonFormatted.scrollRectToVisible(viewRect);
			jsonFormatted.getHighlighter().removeHighlight(allPosTag.get(posEnterCounter - 1));
			allPosTag.remove(allPosTag.get(posEnterCounter - 1));
			allPosTag.add(posEnterCounter - 1, jsonFormatted.getHighlighter().addHighlight(
					allPos.get(posEnterCounter - 1), allPos.get(posEnterCounter - 1) + findLength, painterYellow));
			posEnterCounter++;
		} catch (Exception e) {
		}
		totalMsgDisplay.setText("Match '" + posEnterCounter + "' of '" + allPosTag.size() + "'");
	}

	public static void highlightPrevious() {
		directionMemory.add(0, false);
		directionMemory.remove(2);
		try {
			if (directionMemory.get(1))
				posEnterCounter--;
			jsonFormatted.getHighlighter().removeHighlight(allPosTag.get(posEnterCounter - 1));
			allPosTag.remove(allPosTag.get(posEnterCounter - 1));
			allPosTag.add(posEnterCounter - 1, jsonFormatted.getHighlighter().addHighlight(
					allPos.get(posEnterCounter - 1), allPos.get(posEnterCounter - 1) + findLength, painterPink));
			totalMsgDisplay.setText("Match '" + (posEnterCounter) + "' of '" + allPosTag.size() + "'");
			jsonFormatted.getHighlighter().removeHighlight(allPosTag.get(posEnterCounter));
			allPosTag.remove(allPosTag.get(posEnterCounter));
			allPosTag.add(posEnterCounter, jsonFormatted.getHighlighter().addHighlight(allPos.get(posEnterCounter),
					allPos.get(posEnterCounter) + findLength, painterYellow));
			Rectangle viewRect = jsonFormatted.modelToView(allPos.get(posEnterCounter - 1));
			jsonFormatted.scrollRectToVisible(viewRect);
			if (posEnterCounter != 1)
				posEnterCounter--;
		} catch (Exception e) {
		}
	}

	public static void uponPressingEnter() throws Exception {
		directionMemory.add(0, true);
		directionMemory.remove(2);
		String find = findTf.getText().toLowerCase();
		currentFind = find;
		findLength = find.length();
		if (!(previousFind.equalsIgnoreCase("")) && !(currentFind.equalsIgnoreCase(previousFind))) {
			// This is a fresh find
			resetEverything();
		}
		previousFind = currentFind;
		if (!isHishlighted) {
			if (find != null && find.length() > 0) {
				Document document = jsonFormatted.getDocument();
				try {
					if (pos + findLength > document.getLength())
						pos = 0;
					while (pos + findLength <= document.getLength()) {
						String match = document.getText(pos, findLength).toLowerCase();
						if (match.equals(find)) {
							try {
								isHishlighted = true;
								allPosTag.add(jsonFormatted.getHighlighter().addHighlight(pos, pos + findLength,
										painterYellow));
							} catch (Exception e) {
							}
							;
							allPos.add(pos);
						}
						pos++;
					}

					try {
						System.out.println(allPosTag.size());
						if (allPosTag.size() == 0)
							totalMsgDisplay.setText("No match found");
						else
							totalMsgDisplay.setText("Match '" + 1 + "' of '" + allPosTag.size() + "'");
						if (allPosTag.size() > 0) {
							jsonFormatted.getHighlighter().removeHighlight(allPosTag.get(0));
							allPosTag.remove(0);
							allPosTag.add(0,
									jsonFormatted.getHighlighter().addHighlight(allPos.get(posEnterCounter - 1),
											allPos.get(posEnterCounter - 1) + findLength, painterPink));
							Rectangle viewRect = jsonFormatted.modelToView(allPos.get(0));
							jsonFormatted.scrollRectToVisible(viewRect);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception exp) {
					exp.printStackTrace();
				}
			}
		} else
			highlightNext();
	}

	public static String sendPostRequest(String requestUrl, String request) {
		StringBuffer jsonString = null;
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			writer.write(request);
			writer.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			jsonString = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				jsonString.append(line);
			}
			br.close();
			connection.disconnect();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return jsonString.toString();
	}

}
