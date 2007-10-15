/*
 * Copyright (C) 2007 IsmAvatar <cmagicj@nni.com>
 * 
 * This file is part of Lateral GM.
 * Lateral GM is free software and comes with ABSOLUTELY NO WARRANTY.
 * See LICENSE for details.
 */

package org.lateralgm.subframes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.lateralgm.compare.ResourceComparator;
import org.lateralgm.components.GMLTextArea;
import org.lateralgm.components.impl.ResNode;
import org.lateralgm.main.LGM;
import org.lateralgm.messages.Messages;
import org.lateralgm.resources.Timeline;
import org.lateralgm.resources.sub.Argument;
import org.lateralgm.resources.sub.Moment;

public class TimelineFrame extends ResourceFrame<Timeline> implements ActionListener,
		ListSelectionListener
	{
	private static final long serialVersionUID = 1L;

	public JButton add;
	public JButton change;
	public JButton delete;
	public JButton duplicate;
	public JButton shift;
	public JButton merge;
	public JButton clear;

	public JList moments;
	public GmObjectFrame.ActionList actions;
	public GMLTextArea code;

	public TimelineFrame(Timeline res, ResNode node)
		{
		super(res,node);

		setSize(560,400);
		setMinimumSize(new Dimension(560,400));
		setLayout(new BoxLayout(getContentPane(),BoxLayout.X_AXIS));

		JPanel side1 = new JPanel(new FlowLayout());
		side1.setPreferredSize(new Dimension(180,280));
		side1.setMaximumSize(new Dimension(180,Integer.MAX_VALUE));

		JLabel lab = new JLabel(Messages.getString("TimelineFrame.NAME")); //$NON-NLS-1$
		lab.setPreferredSize(new Dimension(160,14));
		side1.add(lab);
		name.setPreferredSize(new Dimension(160,20));
		side1.add(name);

		addGap(side1,180,20);

		add = new JButton(Messages.getString("TimelineFrame.ADD")); //$NON-NLS-1$
		add.setPreferredSize(new Dimension(80,20));
		add.addActionListener(this);
		side1.add(add);
		change = new JButton(Messages.getString("TimelineFrame.CHANGE")); //$NON-NLS-1$
		change.setPreferredSize(new Dimension(80,20));
		change.addActionListener(this);
		side1.add(change);
		delete = new JButton(Messages.getString("TimelineFrame.DELETE")); //$NON-NLS-1$
		delete.setPreferredSize(new Dimension(80,20));
		delete.addActionListener(this);
		side1.add(delete);
		duplicate = new JButton(Messages.getString("TimelineFrame.DUPLICATE")); //$NON-NLS-1$
		duplicate.setPreferredSize(new Dimension(90,20));
		duplicate.addActionListener(this);
		side1.add(duplicate);

		addGap(side1,180,20);

		shift = new JButton(Messages.getString("TimelineFrame.SHIFT")); //$NON-NLS-1$
		shift.setPreferredSize(new Dimension(80,20));
		shift.addActionListener(this);
		side1.add(shift);
		merge = new JButton(Messages.getString("TimelineFrame.MERGE")); //$NON-NLS-1$
		merge.setPreferredSize(new Dimension(80,20));
		merge.addActionListener(this);
		side1.add(merge);
		clear = new JButton(Messages.getString("TimelineFrame.CLEAR")); //$NON-NLS-1$
		clear.setPreferredSize(new Dimension(80,20));
		clear.addActionListener(this);
		side1.add(clear);

		addGap(side1,180,50);

		save.setPreferredSize(new Dimension(130,24));
		save.setText(Messages.getString("TimelineFrame.SAVE")); //$NON-NLS-1$
		side1.add(save);

		JPanel side2 = new JPanel(new BorderLayout());
		side2.setMaximumSize(new Dimension(90,Integer.MAX_VALUE));
		lab = new JLabel(Messages.getString("TimelineFrame.MOMENTS")); //$NON-NLS-1$
		side2.add(lab,"North");
		moments = new JList(res.moments.toArray());
		moments.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		moments.addListSelectionListener(this);
		JScrollPane scroll = new JScrollPane(moments);
		scroll.setPreferredSize(new Dimension(90,260));
		side2.add(scroll,"Center");

		add(side1);
		add(side2);

		if (false)
			{
			code = new GMLTextArea("");
			JScrollPane codePane = new JScrollPane(code);
			add(codePane);
			}
		else
			{
			actions = GmObjectFrame.addActionPane(this);
			}

		moments.setSelectedIndex(0);
		}

	public void saveMoments()
		{
		actions.save();
		res.moments.clear();
		ListModel lm = moments.getModel();
		for (int i = 0; i < lm.getSize(); i++)
			{
			Object o = lm.getElementAt(i);
			Moment m = (Moment) o;
			res.moments.add(m);
			}
		}

	@Override
	public boolean resourceChanged()
		{
		commitChanges();
		ResourceComparator c = new ResourceComparator();
		c.addExclusions(Argument.class,"editor");
		return c.areEqual(res,resOriginal);
		}

	@Override
	public void revertResource()
		{
		LGM.currentFile.timelines.replace(res,resOriginal);
		}

	//TODO:
	public void commitChanges()
		{
		saveMoments();
		res.setName(name.getText());
		}

	//TODO:
	public void actionPerformed(ActionEvent e)
		{
		if (e.getSource() == add)
			{
			return;
			}
		super.actionPerformed(e);
		}

	//Moments selection changed
	public void valueChanged(ListSelectionEvent e)
		{
		if (e.getValueIsAdjusting()) return;
		Moment m = (Moment) moments.getSelectedValue();
		actions.setActionContainer(m);
		}
	}
