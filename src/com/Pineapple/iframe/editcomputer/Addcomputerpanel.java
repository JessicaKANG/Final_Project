package com.Pineapple.iframe.editcomputer;

import java.awt.Dimension;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.Pineapple.Dao.DBAddcomputer;
import com.Pineapple.Dao.DBCheckcomponent;
import com.Pineapple.Dao.model.Computer;

public class Addcomputerpanel extends JPanel {
	private JTextField textFieldID;
	private JTextField textFieldName;
	private JTextField textFieldPrice;
	private JTextField textFieldPicture;
	private JComboBox comboboxType;
	private JComboBox comboboxColor;
	private JComboBox comboboxSize;
	private JComboBox comboboxStock;
	private JComboBox comboboxMemory;
	private JComboBox comboboxGraphics;
	private JComboBox comboboxProcessor;
	private JButton resetButton;
	
	private Computer computer;
	private String id;
	private String name;
	private String type;
	private float price;
	private String picture;
	
	
	public Addcomputerpanel(){
		
		super();//先构造一个panel
		
		//设置尺寸和布局，并将它显示出来
		setBounds(10, 10, 460, 300);
		setLayout(new GridBagLayout());
		setVisible(true);
		
		//所有的标签都设成final
		final JLabel computerID = new JLabel();
		computerID.setText("型号：");
		setupComponent(computerID, 0, 0, 1, 0, false);
		textFieldID = new JTextField();
		// 定位型号输入文本框
		setupComponent(textFieldID, 1, 0, 5, 200, true);
		////////////////////////////////////////////////////////
		final JLabel computerName = new JLabel("名称：");
		setupComponent(computerName, 0, 1, 1, 0, false);
		textFieldName = new JTextField();
		// 定位名称输入文本框
		setupComponent(textFieldName, 1, 1, 5, 100, true);
		////////////////////////////////////////////////////////
		final JLabel computerPrice = new JLabel();
		computerPrice.setText("价格：");
		setupComponent(computerPrice, 0, 2, 1, 0, false);
		textFieldPrice = new JTextField();
		// 定位价格输入文本框
		setupComponent(textFieldPrice, 1, 2, 2, 100, true);
		////////////////////////////////////////////////////////
		final JLabel computerType = new JLabel();
		computerType.setText("分类:");
		setupComponent(computerType,3,2,1,0,false);
		comboboxType = new JComboBox();
		comboboxType.setModel(new DefaultComboBoxModel(new String[]{"DESKTOP",
				"LAPTOP", "SERVER"}));
		comboboxType.setPreferredSize(new Dimension(120, 21));
		// 定位电脑分类的下拉选择框
		setupComponent(comboboxType, 4, 2, 1, 0, true);
		//////////////////////////////////////////////////////////
		final JLabel computerConfig = new JLabel();
		computerConfig.setText("配置信息:");
		setupComponent(computerConfig,0,3,1,0,false);
		//////////////////////////////////////////////////////////
		final JLabel configColor = new JLabel();
		configColor.setText("颜色:");
		setupComponent(configColor,0,4,1,0,false);
		comboboxColor = new JComboBox();
		List<String> colorlist = searchitemlist("Color");
		setupCombo(comboboxColor,colorlist);		
		comboboxColor.setPreferredSize(new Dimension(120, 21));
		// 定位
		setupComponent(comboboxColor, 1, 4, 1, 0, true);
		//*********************************************
		final JLabel configSize = new JLabel();
		configSize.setText("尺寸:");
		setupComponent(configSize,3,4,1,0,false);
		comboboxSize = new JComboBox();
		List<String> sizelist = searchitemlist("Screen");
		setupCombo(comboboxSize,sizelist);
		comboboxSize.setPreferredSize(new Dimension(120, 21));
		// 定位
		setupComponent(comboboxSize, 4, 4, 1, 0, true);

		//////////////////////////////////////////////////////////
		final JLabel configStock = new JLabel();
		configStock.setText("硬盘:");
		setupComponent(configStock,0,5,1,0,false);
		comboboxStock = new JComboBox();
		List<String> stocklist = searchitemlist("Stock");
		setupCombo(comboboxStock,stocklist);
		comboboxStock.setPreferredSize(new Dimension(120, 21));
		// 定位
		setupComponent(comboboxStock, 1, 5, 1, 0, true);
		//*********************************************
		final JLabel configMemory = new JLabel();
		configMemory.setText("内存:");
		setupComponent(configMemory,3,5,1,0,false);
		comboboxMemory = new JComboBox();
		List<String> memorylist = searchitemlist("Memory");
		setupCombo(comboboxMemory,memorylist);
		comboboxMemory.setPreferredSize(new Dimension(120, 21));
		// 定位
		setupComponent(comboboxMemory, 4, 5, 1, 0, true);
		//////////////////////////////////////////////////////////
		final JLabel configGraphics = new JLabel();
		configGraphics.setText("显卡:");
		setupComponent(configGraphics,0,6,1,0,false);
		comboboxGraphics = new JComboBox();
		List<String> graphicslist = searchitemlist("Graphics");
		setupCombo(comboboxGraphics,graphicslist);
		comboboxGraphics.setPreferredSize(new Dimension(120, 21));
		// 定位
		setupComponent(comboboxGraphics, 1, 6, 1, 0, true);
		//*********************************************
		final JLabel configProcessor = new JLabel();
		configProcessor.setText("处理器:");
		setupComponent(configProcessor,3,6,1,0,false);
		comboboxProcessor = new JComboBox();
		List<String> processorlist = searchitemlist("Processor");
		setupCombo(comboboxProcessor,processorlist);
		comboboxProcessor.setPreferredSize(new Dimension(120, 21));
		// 定位
		setupComponent(comboboxProcessor, 4, 6, 1, 0, true);
		///////////////////////////////////////////////////////////////////
		final JLabel computerPicture = new JLabel();
		computerPicture.setText("图片：");
		setupComponent(computerPicture, 0, 7, 1, 0, false);
		textFieldPicture = new JTextField();
		// 定位图片地址输入文本框
		setupComponent(textFieldPicture, 1, 7, 5, 100, true);
		
/////////////////////////////////////////////////////////////////////////////////////////////
		//初始化下拉菜单
		comboboxType.setSelectedItem(null);
		comboboxColor.setSelectedItem(null);
		comboboxSize.setSelectedItem(null);
		comboboxStock.setSelectedItem(null);
		comboboxMemory.setSelectedItem(null);
		comboboxGraphics.setSelectedItem(null);
		comboboxProcessor.setSelectedItem(null);
///////////////////////////////////////////////////////////////////////////////////////////
		//添加按钮
		final JButton addButton = new JButton();
		//添加按钮的事件监听
		addButton.addActionListener(new ActionListener() {
			//先判断信息是否齐全，再执行数据库操作
			public void actionPerformed(final ActionEvent e) {
				if (textFieldID.getText().equals("")
						|| textFieldName.getText().equals("")
						|| textFieldPrice.getText().equals("")) {
					JOptionPane.showMessageDialog(Addcomputerpanel.this,
							"请完成未填写的信息。", "商品添加", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				try {
					id = textFieldID.getText();
					
					if (DBAddcomputer.exists(id)) {
						JOptionPane.showMessageDialog(Addcomputerpanel.this,
								"该型号已存在", "添加商品失败",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				Computer computer = new Computer();
				computer.setId(textFieldID.getText());
				computer.setName(textFieldName.getText());
				computer.setType(comboboxType.getSelectedItem().toString());
				computer.setPrice(Float.parseFloat(textFieldPrice.getText()));
				computer.setPicture(textFieldPicture.getText());
				String id_computer = textFieldID.getText();
				
				String name_color = comboboxColor.getSelectedItem().toString();
				String id_color = DBCheckcomponent.getComponentID(name_color);
				
				String name_size = comboboxSize.getSelectedItem().toString();
				String id_size = DBCheckcomponent.getComponentID(name_size);

				String name_stock = comboboxStock.getSelectedItem().toString();
				String id_stock = DBCheckcomponent.getComponentID(name_stock);

				String name_memory = comboboxMemory.getSelectedItem().toString();
				String id_memory = DBCheckcomponent.getComponentID(name_memory);
	
				String name_graphics = comboboxGraphics.getSelectedItem().toString();
				String id_graphics = DBCheckcomponent.getComponentID(name_graphics);

				String name_processor = comboboxProcessor.getSelectedItem().toString();
				String id_processor = DBCheckcomponent.getComponentID(name_processor);
				
				try {					
					if (DBAddcomputer.save(computer)&&DBAddcomputer.savecomputerconfig(id_computer, id_color)
							&&DBAddcomputer.savecomputerconfig(id_computer, id_size)&&
							DBAddcomputer.savecomputerconfig(id_computer, id_stock)&&
							DBAddcomputer.savecomputerconfig(id_computer, id_memory)&&
							DBAddcomputer.savecomputerconfig(id_computer, id_graphics)&&
							DBAddcomputer.savecomputerconfig(id_computer, id_processor)) {
						JOptionPane.showMessageDialog(Addcomputerpanel.this,
								"恭喜，商品已成功添加入库", "商品添加成功",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			
		
	
			}
		});
		addButton.setText("添加");
		setupComponent(addButton, 1, 8, 1, 1, false);
		
		resetButton = new JButton();
		resetButton.setText("重置");
		setupComponent(resetButton, 4, 8, 1, 1, false);
		// 重添按钮的事件监听类
				resetButton.addActionListener(new ActionListener() {
					public void actionPerformed(final ActionEvent e) {
						textFieldID.setText("");
						textFieldName.setText("");
						textFieldPrice.setText("");
						textFieldPicture.setText("");
						comboboxType.setSelectedItem(null);
						comboboxColor.setSelectedItem(null);
						comboboxSize.setSelectedItem(null);
						comboboxStock.setSelectedItem(null);
						comboboxMemory.setSelectedItem(null);
						comboboxGraphics.setSelectedItem(null);
						comboboxProcessor.setSelectedItem(null);
					}
				});
				
		
		
	}
	/**
	 * 设置组件位置并添加到容器中的方法
	 * @param component
	 * @param gridx
	 * @param gridy
	 * @param gridwidth
	 * @param ipadx
	 * @param fill
	 */
	private void setupComponent(JComponent component, int gridx, int gridy,
			int gridwidth, int ipadx, boolean fill) {
		final GridBagConstraints gridBagConstrains = new GridBagConstraints();
		gridBagConstrains.gridx = gridx;
		gridBagConstrains.gridy = gridy;
		gridBagConstrains.insets = new Insets(5, 1, 3, 1);//设置最小间距
		if (gridwidth > 1)
			gridBagConstrains.gridwidth = gridwidth;//设置宽度
		if (ipadx > 0)
			gridBagConstrains.ipadx = ipadx;
		if (fill)
			gridBagConstrains.fill = GridBagConstraints.HORIZONTAL;//横向加长文本框以满足输入需求
		add(component, gridBagConstrains);
		
		
	}
	
	/**
	 * 把项目串放到combobox里
	 * @param combobox
	 * @param itemstring
	 */
	public void setupCombo (JComboBox comboBox, List<String> itemlist){
		Iterator iterator = itemlist.iterator();
		while(iterator.hasNext()){
			String item = (String) iterator.next();
			comboBox.addItem(item);
		}
		
	}
	/**
	 * 向数据库查询配件可选项
	 * @param name
	 * @return
	 */
	public List<String> searchitemlist(String name){
    	    List<String> itemlist = null ;
    	    itemlist = DBCheckcomponent.getComponentNameList(name);
    	    return itemlist;
			
	}
	

}
