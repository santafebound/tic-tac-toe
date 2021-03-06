package com.martinerlic.tic_tac_toe.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.martinerlic.tic_tac_toe.R;
import com.martinerlic.tic_tac_toe.activity.MainActivity;
import com.martinerlic.tic_tac_toe.model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.ViewHolder> {


    /* Globals */
    private String[] mData;
    private int mColumns;
    private Player mPlayer1;
    private Player mPlayer2;
    private Context mContext;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private List<Integer> mXPositions;
    private List<Integer> mOPositions;
    private TextView mPlayerHint;


    /* Initialize constructor */
    public GridRecyclerAdapter(Context context, String[] data, int numColumns, Player player1, Player player2, List<Integer> xPositions, List<Integer> oPositions, TextView playerHint) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mColumns = numColumns;
        this.mPlayer1 = player1;
        this.mPlayer2 = player2;
        this.mContext = context;
        this.mXPositions = xPositions;
        this.mOPositions = oPositions;
        this.mPlayerHint = playerHint;
    }


    /* Inflate cell layout */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);

        /* Set minimum height of each cell by the number of rows in the grid */
        int height = parent.getMeasuredHeight() / mColumns;
        itemView.setMinimumHeight(height);

        return new ViewHolder(itemView);
    }


    /* Bind data to TextView */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        String cell = mData[position];
        viewHolder.cellTextView.setText(cell);

        List<Integer[]> conditionList = new ArrayList<>();

        viewHolder.itemView.setOnClickListener(v -> {
            checkTurn(viewHolder, position);
            checkCompletionConditions(mXPositions, mOPositions, conditionList);
        });
    }


    /* Check the current player turn */
    private void checkTurn(final ViewHolder viewHolder, int position) {
        if (!(viewHolder.cellTextView.getText().toString().isEmpty())) {
            Toast.makeText(mContext, "You cannot select this cell!", Toast.LENGTH_SHORT).show();
        } else {
            if (mPlayer1.isTurn()) {
                /* Set cell to "X" */
                viewHolder.cellTextView.setText(mPlayer1.getTextValue());

                /* Set clicked */
                viewHolder.cellTextView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                mXPositions.add(position);
                // mOPositions.add(-1);
                // Toast.makeText(mContext, mXPositions.toString(), Toast.LENGTH_SHORT).show();

                mPlayerHint.setText(R.string.player_2_turn); // Indicate that it is Player 2's turn next

                /* Prepare next turn */
                mPlayer1.setTurn(false);
                mPlayer2.setTurn(true);
            } else {
                /* Set cell to "O" */
                viewHolder.cellTextView.setText(mPlayer2.getTextValue());

                /* Set clicked */
                viewHolder.cellTextView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
                mOPositions.add(position);
                // mXPositions.add(-1);
                // Toast.makeText(mContext, mOPositions.toString(), Toast.LENGTH_SHORT).show();

                mPlayerHint.setText(R.string.player_1_turn); // Indicate that it is Player 1's turn next

                /* Prepare next turn */
                mPlayer1.setTurn(true);
                mPlayer2.setTurn(false);
            }
        }
    }


    private void checkCompletionConditions(List<Integer> mXPositions, List<Integer> mOPositions, List<Integer[]> conditionList) {
        /* Legend
        * 0: Top-left
        * 1: Top-center
        * 2: Top-right
        * 3: Middle-left
        * 4: Middle-center
        * 5: Middle-right
        * 6: Bottom-left
        * 7: Bottom-center
        * 8: Bottom-right
        */

        /* Win Conditions
        *
        * Horizontal Lines
        * Condition 1: 0, 1, 2
        * Condition 2: 3, 4, 5
        * Condition 3: 6, 7, 8
        *
        * Vertical Lines
        * Condition 4: 0, 3, 6
        * Condition 5: 1, 4, 7
        * Condition 6: 2, 5, 8
        *
        * Diagonal Lines
        * Condition 7: 0, 4, 8
        * Condition 8: 2, 4, 6
        */

        /* Define the winning conditions */
        Integer[] condition1 = {0, 1, 2};
        Integer[] condition2 = {3, 4, 5};
        Integer[] condition3 = {6, 7, 8};

        Integer[] condition4 = {0, 3, 6};
        Integer[] condition5 = {1, 4, 7};
        Integer[] condition6 = {2, 5, 8};

        Integer[] condition7 = {0, 4, 8};
        Integer[] condition8 = {2, 4, 6};

        /* Add conditions lists to list of lists */
        conditionList.add(condition1);
        conditionList.add(condition2);
        conditionList.add(condition3);

        conditionList.add(condition4);
        conditionList.add(condition5);
        conditionList.add(condition6);

        conditionList.add(condition7);
        conditionList.add(condition8);

        /* Finally, check if a player has won */
        for (Integer[] conditionItem : conditionList) {
            if (mXPositions.containsAll(Arrays.asList(conditionItem))) {
                Toast.makeText(mContext, R.string.player_1_wins, Toast.LENGTH_SHORT).show();

                clearXValues();
                clearOValues();

                startNewGame(mPlayer1, mPlayer2);
            } else //noinspection StatementWithEmptyBody
                if (mOPositions.containsAll(Arrays.asList(conditionItem))) {
                Toast.makeText(mContext, R.string.player_2_wins, Toast.LENGTH_SHORT).show();

                clearXValues();
                clearOValues();

                startNewGame(mPlayer2, mPlayer1);
            } else {
                // TODO: Tie-condition. If there are no more empty itemViews left, and the above conditions do not hold, show a draw
                /*if () {
                    Toast.makeText(mContext, R.string.player_2_wins, Toast.LENGTH_SHORT).show();

                    clearXValues();
                    clearOValues();

                    if (mPlayer1.isTurn()) {
                        startNewGame(mPlayer2, mPlayer1);
                    } else {
                        startNewGame(mPlayer1, mPlayer2);
                    }
                }*/

            }
        }

    }


    private void clearOValues() {
        int size = this.mOPositions.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.mOPositions.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }


    private void clearXValues() {
        int size = this.mXPositions.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.mXPositions.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }


    /* Select the player to start the new game, then wipe the board! */
    private void startNewGame(Player playerToStart, Player nextPlayer) {
        ((MainActivity) mContext).initRecyclerView(mData);
        playerToStart.setTurn(true);
        nextPlayer.setTurn(false);

        if (playerToStart.getTextValue().equals("X")) {
            mPlayerHint.setText(R.string.player_1_turn);
        } else {
            mPlayerHint.setText(R.string.player_2_turn);
        }
    }


    /* Item count */
    @Override
    public int getItemCount() {
        return mData.length;
    }


    /* Store cell views */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView cellTextView;

        ViewHolder(View itemView) {
            super(itemView);
            cellTextView = (TextView) itemView.findViewById(R.id.textView);
        }


        @Override
        public void onClick(View itemView) {
            if (mClickListener != null) mClickListener.onItemClick(itemView, getAdapterPosition());
        }
    }


    /* Get data at click position */
    public String getItem(int id) {
        return mData[id];
    }


    /* Capture click position */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    /* Implement method to respond to click events in MainActivity */
    public interface ItemClickListener {
        void onItemClick(View itemView, int position);
    }


}